package com.example.harvester.model.entities.realm_entities.classifier_object.characteristic

import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

//  -- Описание объекта элемента справочника Номенклатуры --
open class Characteristic (
    // -- Характеристики класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString(),
    var code: String = "",
    var description: String = "",

    // @product - какому товару принадлежит данная характеристика
    var product: Product? = null
): RealmObject()