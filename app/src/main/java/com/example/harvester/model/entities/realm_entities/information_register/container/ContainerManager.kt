package com.example.harvester.model.entities.realm_entities.information_register.container

import io.realm.Realm
import io.realm.kotlin.where

// MARK: Поиск и загрузка данных в справочник
fun Container.ffetch(barcode: String?, containerBarcode: String = ""): Container? {

    // MARK: Если код транспортной упаковки отсутствует, возвращаем NULL
    if(barcode.isNullOrEmpty()) return null

    val realm = Realm.getDefaultInstance()
    // MARK: Ищем запись по коду транспортной упаковки или создаем ее
    var obj = Container().fetch(barcode)
    lateinit var containerRef: Container
    if(obj == null){
        obj = Container()
        obj.barcode = barcode
        realm.beginTransaction()
        containerRef = realm.copyToRealm(obj)
        realm.commitTransaction()
    }
    else
        return obj

    // -- Обновление информации о родительской транспортной упаковке рекурсивно --
    if(containerBarcode.isNullOrEmpty()){
        realm.beginTransaction()
        containerRef.containerBarcode = Container().ffetch(barcode = containerBarcode)
        realm.commitTransaction()
    }

    realm.close()
    return containerRef
}

fun Container.fetch(containerBarcode: Container):Container? {
    val realm = Realm.getDefaultInstance()
    val objects = realm.where<Container>().like("containerBarcode.uuid", containerBarcode.uuid)
    realm.close()
    return objects.findFirst()
}

fun Container.fetch(barcode: String): Container? {
    val realm = Realm.getDefaultInstance()
    val objects = realm.where<Container>().equalTo("barcode", barcode)
    realm.close()
    return objects.findFirst()
}