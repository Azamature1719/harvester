package com.example.harvester.model.entities.realm_entities.information_register.data_harvested

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.example.harvester.model.entities.realm_entities.information_register.container.Container
import io.realm.Realm
import io.realm.kotlin.where

// MARK: Обновление учетного количества в регистре по ключевым реквизитам. Используется для загрузки данных из ERP системы
fun DataHarvested.update(desc: String?,
                         product: Product?,
                         characteristic: Characteristic?,
                         container: Container?,
                         quantityAcc: Double): DataHarvested?{
    if(quantityAcc == 0.0) return null

    val realm =  Realm.getDefaultInstance()
    var objects = realm.where<DataHarvested>()

    // QUESTION: Почему такой выбор - КМ или ШХ?
    if(product == null){
        objects.equalTo("desc", desc).and()
            .equalTo("product.uuid", product?.uuid)
    } else{
        objects.equalTo("product.uuid", product.uuid).and()
            .equalTo("characteristic.uuid", characteristic?.uuid).and()
            .equalTo("container.uuid", container?.uuid)
    }

    var objectFirst = objects.findFirst()
    return if(objectFirst != null){
        objectFirst.quantityAcc += quantityAcc
        objectFirst
    } else{
        objectFirst = DataHarvested()
        objectFirst.desc = desc
        objectFirst.product = product
        objectFirst.characteristic = characteristic
        objectFirst.container = container
        objectFirst.quantityAcc += quantityAcc
        realm.executeTransactionAsync{
            realm.copyToRealm(objectFirst)
        }
        realm.close()
        objectFirst
    }
}

// MARK: Поиск записи регистра по ключевым полям и создание при отсутствии
fun DataHarvested.ffetch(desc: String): DataHarvested{
    val fetchedObject = DataHarvested().fetch(desc)
    if(fetchedObject != null) return fetchedObject

    var dataHarvestedRecord = DataHarvested()
    dataHarvestedRecord.desc = desc

    var realm = Realm.getDefaultInstance()
    lateinit var recordCopy:DataHarvested
    realm.executeTransaction {
        recordCopy = realm.copyToRealm(dataHarvestedRecord)
    }
    return recordCopy
}

fun DataHarvested.ffetch(product: Product,
                                      characteristic: Characteristic,
                                      container: Container?): DataHarvested{

    val realm = Realm.getDefaultInstance()
    val objects =  realm.where<DataHarvested>()
        .equalTo("product.uuid", product.uuid).and()
        .equalTo("characteristic", characteristic.uuid)
        .findFirst()

    if(objects != null){
        if (objects.quantityAcc == 0.0 ||
            objects.quantity < objects.quantityAcc)
            return objects
    }

    var dataHarvestedRecord = DataHarvested()
    dataHarvestedRecord.characteristic = characteristic
    dataHarvestedRecord.container = container
    dataHarvestedRecord.product = product

    realm.beginTransaction()
    var dataHarvestedRef = realm.copyToRealm(dataHarvestedRecord)
    realm.commitTransaction()
    realm.close()

    return dataHarvestedRef
}

// MARK: Поиск записи регистра по ключевому полю desc
fun DataHarvested.fetch(desc: String):DataHarvested?{
    val realm = Realm.getDefaultInstance()
    val objects = realm.where<DataHarvested>().equalTo("desc", desc).findFirst()
    realm.close()
    return objects
}

// MARK: Поиск записи регистра по ключевым полям product, characteristic, container
fun DataHarvested.fetch(product: Product?, characteristic: Characteristic?, container: Container?): DataHarvested?{
    val realm = Realm.getDefaultInstance()

    if(product == null || characteristic == null || container == null) return null

    val objects = realm.where<DataHarvested>()
        .equalTo("product.uuid", product.uuid).and()
        .equalTo("characteristic.uuid", characteristic.uuid).and()
        .equalTo("container.uuid", container.uuid)
        .findFirst()

    realm.close()
    return objects
}
