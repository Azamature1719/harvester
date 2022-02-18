package com.example.harvester.model.entities.realm_entities.information_register.barcode

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.Realm
import io.realm.kotlin.where

fun Barcode.ffetch(barcode: String, product: Product?, characteristic: Characteristic?): Barcode{

    val fetchedBarcode = fetch(barcode)
    if(fetchedBarcode != null) return fetchedBarcode

    val record = Barcode()
    record.barcode = barcode
    record.product = product
    record.characteristic = characteristic

    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    val barcodeRef = realm.copyToRealm(record)
    realm.commitTransaction()
    realm.close()

    return barcodeRef
}

fun Barcode.fetch(barcode: String): Barcode?{
    val realm = Realm.getDefaultInstance()
    val objects = realm.where<Barcode>().equalTo("barcode", barcode)
    realm.close()
    return objects.findFirst()
}