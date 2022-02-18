package com.example.harvester.model.entities.realm_entities.information_register.barcode

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание записи регистра Штрихкоды упаковок и товаров --
open class Barcode: RealmObject(){

    // -- Характеристики объекта класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
    var currentDate: Date = Date()
    //  var dateText: String = ""

    //  @barcode - штрихкод
    //  @product - товарная номенклатура (часть 1 связки)
    //  @characteristic - характеристика товара (часть 2 связки)
    var barcode:String = ""
    var product: Product? = null
    var characteristic: Characteristic? = null
}