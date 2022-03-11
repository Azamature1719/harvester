package com.example.harvester.model.entities.realm_entities.information_register.price

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание записи регистра Цены номенклатуры --
open class Price(
    // -- Характеристики объекта класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString(),
    var currentDate: Date = Date(),
    // var dateText: String = ""

    // @product - товар, цена которого задаётся (1 часть связки)
    // @characteristic - характеристика данного товара (2 часть связки)
    // @price - цена товара
    var product: Product? = null,
    var characteristic: Characteristic? = null,
    var price: Double = 0.0
): RealmObject()