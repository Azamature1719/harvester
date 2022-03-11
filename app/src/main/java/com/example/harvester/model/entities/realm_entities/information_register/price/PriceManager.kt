package com.example.harvester.model.entities.realm_entities.information_register.price

import com.example.harvester.MainActivity
import com.example.harvester.framework.App
import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.vicpin.krealmextensions.*
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Price.update(price: Double, product: Product?, characteristic: Characteristic?) {
    if(price == 0.0) return
    if(fetch(product, characteristic) == null) {
        Price(price = price.toDouble(),
            product = product,
            characteristic = characteristic)
            .save()
    }
}

// MARK: Поиск записи регистра по номенклатуре и характеристике
fun Price.fetch(product: Product?, characteristic: Characteristic?): Price? {
    if(product == null || characteristic == null) return null
    return Price().queryFirst() {
        equalTo("product.uuid", product.uuid)
        .and()
        .equalTo("characteristic.uuid", characteristic.uuid)
    }
}

// MARK: Полуение цены по номенклатуре и характеристике
fun Price.price(product: Product?, characteristic: Characteristic?): Double? {
    if(product == null && characteristic == null) return 0.0
    val obj = fetch(product, characteristic) // MARK: Если NULL, возвращается 0.0, в противном случае - значение price
    return obj?.price ?: 0.0
}