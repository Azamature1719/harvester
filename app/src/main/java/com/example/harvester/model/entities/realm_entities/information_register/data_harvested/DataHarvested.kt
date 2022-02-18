package com.example.harvester.model.entities.realm_entities.information_register.data_harvested

import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.example.harvester.model.entities.realm_entities.information_register.container.Container
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание записи регистра Собранные данные --
open class DataHarvested: RealmObject() {

    // -- Характеристики объекта класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
    var currentDate: Date = Date()
    // var dateText: String = ""
    // @desc - описание собранного товара
    // @product - товарная номенклатура (1 часть связки)
    // @characteristic - характеристика товара (2 часть связки)
    // @container - какой транспортной упаковке принадлежит
    // @quantity - количество собранных товаров
    // @quantityAcc - количество, которое требуется собрать
    var desc: String? = ""
    var product: Product? = null
    var characteristic: Characteristic? = null
    var container: Container? = null
    var quantity = 0.0
    var quantityAcc = 0.0
}