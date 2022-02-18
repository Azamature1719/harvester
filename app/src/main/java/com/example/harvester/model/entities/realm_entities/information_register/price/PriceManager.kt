package com.example.harvester.model.entities.realm_entities.information_register.price

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.Realm
import io.realm.kotlin.where

fun Price.update(price: Double, product: Product?, characteristic: Characteristic?) {
    if(price == 0.0) return

    var priceRecord = Price().fetch(product, characteristic)
    if(priceRecord == null) {
        priceRecord = Price()
        priceRecord.price = price.toDouble()
        priceRecord.product = product
        priceRecord.characteristic = characteristic
    }

    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    realm.copyToRealm(priceRecord)
    realm.commitTransaction()
    realm.close()
}

// MARK: Поиск записи регистра по номенклатуре и характеристике
fun Price.fetch(product: Product?, characteristic: Characteristic?): Price? {
    if(product == null || characteristic == null) return null

    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    val objFindByProduct = realm.where<Price>().like("product.uuid", product.uuid)
    val objFindByCharacteristic = objFindByProduct.like("characteristic.uuid", characteristic.uuid)
    realm.close()
    return objFindByCharacteristic.findFirst()
}

// MARK: Полуение цены по номенклатуре и характеристике
fun Price.price(product: Product?, characteristic: Characteristic?): Double? {
    if(product == null && characteristic == null) return 0.0

    // MARK: Если NULL, возвращается 0.0, в противном случае - значение price
    val obj = Price().fetch(product, characteristic)
    return obj?.price ?: 0.0
}