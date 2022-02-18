package com.example.harvester.model.entities.realm_entities.information_register.container

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание записи регистра Штрихкоды транспортных упаковок
open class Container: RealmObject() {

    // -- Характеристики объекта класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
    var currentDate: Date = Date()
    //  var dateText: String = ""

    // @barcode - штрихкод транспортной упаковки
    // @containerBarcode - в какую транспортную упаковку вложена данная упаковка
    var barcode: String = ""
    var containerBarcode:Container? = null
}
