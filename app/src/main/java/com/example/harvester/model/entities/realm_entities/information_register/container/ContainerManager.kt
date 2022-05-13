package com.example.harvester.model.entities.realm_entities.information_register.container

import com.example.harvester.model.entities.realm_extensions.deleteAll
import com.example.harvester.model.entities.realm_extensions.queryFirst
import com.example.harvester.model.entities.realm_extensions.save

fun Container.ffetch(barcode: String?, containerBarcode: String = ""): Container? {

    if(barcode.isNullOrEmpty()) return null // MARK: Если код транспортной упаковки отсутствует, возвращаем NULL
    var container = Container().fetch(barcode)  // MARK: Ищем запись по коду транспортной упаковки или создаем ее

    var containerRef: Container?
    if(container == null){
        containerRef = Container(barcode = barcode).save()
    }
    else
        return container
    // -- Обновление информации о родительской транспортной упаковке рекурсивно --
    if(containerBarcode.isNullOrEmpty()){
        container?.containerBarcode = Container().ffetch(containerBarcode)
    }
    return containerRef
}

fun Container.fetch(containerBarcode: Container):Container? {
    return Container().queryFirst { equalTo("containerBarcode.uuid", containerBarcode.uuid) }
}

fun Container.fetch(barcode: String): Container? {
    return Container().queryFirst { equalTo("barcode", barcode) }
}

fun Container.clear(){
    Container().deleteAll()
}