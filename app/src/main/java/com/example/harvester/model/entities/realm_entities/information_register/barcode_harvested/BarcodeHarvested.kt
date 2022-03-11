package com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested

import com.example.harvester.model.entities.realm_entities.information_register.barcode.Barcode
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.DataHarvested
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание записи регистра Собранные штрихкоды --
open class BarcodeHarvested(
    // -- Характеристики объекта класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString(),
    var currentDate: Date = Date(),
    var dateText: String = "",

    // @owner_guid - для первичной инициализации
    // @owner - товар, штрихкод которого был собран
    // @barcode - штрихкод отсканированного товара
    // @quantity - количество собранных товаров
    // @quantityAcc - количество требуемых к сохранению
    private var owner_guid: String = "00000000-0000-0000-0000-000000000000",
    var owner: DataHarvested? = null,
    var barcode: Barcode? = null,
    var quantity: Double = 0.0,
    var quantityAcc: Double = 0.0,
): RealmObject()