@file:JvmName("BarcodeHarvestedKt")

package com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested

import com.example.harvester.model.entities.realm_entities.information_register.barcode.Barcode
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.DataHarvested
import com.example.harvester.model.entities.realm_extensions.deleteAll
import com.example.harvester.model.entities.realm_extensions.queryFirst
import com.example.harvester.model.entities.realm_extensions.save

// MARK: Обновление учетного количества в регистре по ключевым реквизитам. Используется для загрузки данных из ERP системы
fun BarcodeHarvested.update(owner: DataHarvested, barcode: Barcode, quantityAcc: Double){
    if(quantityAcc == 0.0) return

    val barcodeHarvested = BarcodeHarvested().fetch(barcode)
    if(barcodeHarvested == null){
        BarcodeHarvested(barcode = barcode, owner = owner, quantityAcc = quantityAcc).save()
    }
    else{
        barcodeHarvested.quantityAcc += quantityAcc
        barcodeHarvested.save()
    }
}

// MARK: Поиск записи регистра по строке штрихкода
fun BarcodeHarvested.fetch(barcodeString: String): BarcodeHarvested?{
    val barcode = Barcode().queryFirst { equalTo("barcode", barcodeString) }
    if(barcode == null) return null

    return fetch(barcode)
}

// MARK: Поиск записи регистра по элементу штрихкода
fun BarcodeHarvested.fetch(barcode: Barcode): BarcodeHarvested?{
    return BarcodeHarvested().queryFirst{equalTo("barcode.uuid", barcode.uuid)}
}

// MARK: Поиск записей регистра по владельцу
fun BarcodeHarvested.fetch(owner: DataHarvested): BarcodeHarvested?{
    return BarcodeHarvested().queryFirst{equalTo("owner_guid", owner.uuid)}
}

fun BarcodeHarvested.clear(){
    BarcodeHarvested().deleteAll()
}