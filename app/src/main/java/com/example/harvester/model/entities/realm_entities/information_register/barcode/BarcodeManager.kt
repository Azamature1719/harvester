package com.example.harvester.model.entities.realm_entities.information_register.barcode

import com.example.harvester.MainActivity
import com.example.harvester.framework.App
import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.queryLast
import com.vicpin.krealmextensions.save
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Barcode.ffetch(barcode: String, product: Product?, characteristic: Characteristic?): Barcode{
    val fetchedBarcode = fetch(barcode)
    if(fetchedBarcode != null) return fetchedBarcode

    Barcode(barcode = barcode, product = product, characteristic = characteristic).save()
    return Barcode().queryLast()!!
}

fun Barcode.fetch(barcode: String): Barcode? {
    return Barcode().queryFirst { equalTo("barcode", barcode) }
}