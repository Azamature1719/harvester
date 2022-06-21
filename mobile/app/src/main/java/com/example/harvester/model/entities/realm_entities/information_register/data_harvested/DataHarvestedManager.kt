package com.example.harvester.model.entities.realm_entities.information_register.data_harvested

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.example.harvester.model.entities.realm_entities.information_register.container.Container
import com.example.harvester.model.entities.realm_extensions.deleteAll
import com.example.harvester.model.entities.realm_extensions.queryAll
import com.example.harvester.model.entities.realm_extensions.queryFirst
import com.example.harvester.model.entities.realm_extensions.save

// MARK: Обновление учетного количества в регистре по ключевым реквизитам. Используется для загрузки данных из ERP системы
fun DataHarvested.update(description: String?,
                         product: Product?,
                         characteristic: Characteristic?,
                         container: Container?,
                         quantityAcc: Double): DataHarvested? {
    if(quantityAcc == 0.0) return null

    var dataHarvested: DataHarvested?
    if(product == null){
        dataHarvested = DataHarvested().queryFirst() {
            equalTo("description", description).and().
            equalTo("product.uuid", product?.uuid)
        }
    } else {
        dataHarvested = DataHarvested().queryFirst() {
            equalTo("product.uuid", product.uuid).and().
            equalTo("characteristic.uuid", characteristic?.uuid).and().
            equalTo("container.uuid", container?.uuid)
        }
    }

    if(dataHarvested != null){
        dataHarvested.quantityAcc += quantityAcc
        dataHarvested.save()
        return dataHarvested
    } else {
        return DataHarvested(
            description = description,
            product = product,
            characteristic = characteristic,
            container = container,
            quantityAcc = quantityAcc
        ).save()
    }
}

// MARK: Поиск записи регистра по ключевым полям и создание при отсутствии
fun DataHarvested.ffetch(description: String): DataHarvested{
    val fetchedObject = DataHarvested().fetch(description)
    if(fetchedObject != null) return fetchedObject
    return DataHarvested(description = description).save()!!
}

fun DataHarvested.ffetch(product: Product,
                         characteristic: Characteristic,
                         container: Container?): DataHarvested{
    val dataHarvested = DataHarvested().queryFirst{
        equalTo("product.uuid", product.uuid).and().
        equalTo("characteristic.uuid", characteristic.uuid)}
    if(dataHarvested != null){
        if (dataHarvested.quantityAcc == 0.0 || dataHarvested.quantity < dataHarvested.quantityAcc)
            return dataHarvested
    }
    return DataHarvested(characteristic = characteristic, container = container, product = product).save()!!
}

// MARK: Поиск записи регистра по ключевому полю desc
fun DataHarvested.fetch(description: String):DataHarvested?{
    return DataHarvested().queryFirst{equalTo("description", description)}
}

// MARK: Поиск записи регистра по ключевым полям product, characteristic, container
fun DataHarvested.fetch(product: Product?, characteristic: Characteristic?, container: Container?): DataHarvested?{
    if(product == null || characteristic == null || container == null) return null
    return DataHarvested().queryFirst{
         equalTo("product.uuid", product.uuid).and().
         equalTo("characteristic.uuid", characteristic.uuid).and().
         equalTo("container.uuid", container.uuid)
    }
}

fun DataHarvested.findAll(): List<DataHarvested> {
    return DataHarvested().queryAll()
}

fun DataHarvested.clear(){
    DataHarvested().deleteAll()
}