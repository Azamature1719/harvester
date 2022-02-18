@file:JvmName("BarcodeHarvestedKt")

package com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested

import com.example.harvester.model.entities.realm_entities.information_register.barcode.Barcode
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.DataHarvested
import io.realm.Realm
import io.realm.kotlin.where


// MARK: Обновление учетного количества в регистре по ключевым реквизитам. Используется для загрузки данных из ERP системы
fun BarcodeHarvested.update(owner: DataHarvested, barcode: Barcode, quantityAcc: Double){
    if(quantityAcc == 0.0) return

    // ERROR: FETCH не работает, так как поиск производится с приведением к строке. Выводимая ошибка - Invalid Type "OBJECT"
    val objectRecord = BarcodeHarvested().fetch(barcode)
    if(objectRecord == null){
        val barcodeHarvestedRecord = BarcodeHarvested().apply {
            this.barcode = barcode
            this.owner = owner
            this.quantityAcc += quantityAcc
        }
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealm(barcodeHarvestedRecord)
        realm.commitTransaction()
        realm.close()
    }
    else{
        objectRecord.quantityAcc += quantityAcc
    }
}

// MARK: Поиск записи регистра по строке штрихкода
fun BarcodeHarvested.fetch(barcodeString: String): BarcodeHarvested?{
    val realm = Realm.getDefaultInstance()
    val objects = realm.where<BarcodeHarvested>().equalTo("barcode", barcodeString)
    realm.close()
    return objects.findFirst()
}

// MARK: Поиск записи регистра по элементу штрихкода
fun BarcodeHarvested.fetch(barcode: Barcode): BarcodeHarvested?{
    val realm = Realm.getDefaultInstance()

    // ERROR: Не работает, так как поиск производится с приведением к строке. Выводимая ошибка - Invalid Type "OBJECT"
    val objects = realm.where<BarcodeHarvested>()
        .equalTo("barcode.uuid", barcode.uuid)
    realm.close()
    return objects.findFirst()
}


// MARK: Поиск записей регистра по владельцу
fun BarcodeHarvested.fetch(owner: DataHarvested): BarcodeHarvested?{
    val realm = Realm.getDefaultInstance()
    val objects = realm.where<BarcodeHarvested>().equalTo("owner_guid", owner.uuid)
    realm.close()
    return objects.findFirst()
}