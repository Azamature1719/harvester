package com.example.harvester.model.entities.realm_entities.information_register.barcode

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.example.harvester.model.entities.realm_extensions.deleteAll
import com.example.harvester.model.entities.realm_extensions.queryFirst
import com.example.harvester.model.entities.realm_extensions.save

fun Barcode.ffetch(barcode: String, product: Product?, characteristic: Characteristic?): Barcode{
    val fetchedBarcode = fetch(barcode)
    if(fetchedBarcode != null) return fetchedBarcode

    return Barcode(barcode = barcode, product = product, characteristic = characteristic).save()!!
}

fun Barcode.fetch(barcode: String): Barcode? {
    return Barcode().queryFirst { equalTo("barcode", barcode) }
}

fun Barcode.clear(){
    Barcode().deleteAll()
}