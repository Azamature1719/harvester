package com.example.harvester.model.entities.realm_entities.classifier_object.product

import com.example.harvester.model.entities.realm_entities.product_type.ProductType
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание товара - объекта элемента справочника Номенклатуры --
open class Product(
    // -- Характеристики класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString(),
    var code: String = "",
    var description: String? = "",

    // @marked - товарная категория
    // @_marked - для инициализации товарной категории
    var _marked: Int = ProductType.none.ordinal,
    // var marked: ProductType = ProductType.valueOf(_marked)

    // @article - артикул товара
    var article: String = "",

    // @alcoholKindCode - тип алкогольной продукции
    // @alcoholCode - код типа алкогольной продукции
    // @alcoholCapacity - объём ёмкости алкогольной продукции
    // @alcoholVolume - градус алкогольной продукции
    var alcoholKindCode: String = "",
    var alcoholCode: String = "",
    var alcoholCapacity: Double = 0.0,
    var alcoholVolume: Double = 0.0
): RealmObject()