package com.example.harvester.model.entities.realm_entities.information_register.container

import androidx.core.content.contentValuesOf
import com.example.harvester.MainActivity
import com.example.harvester.framework.App
import com.vicpin.krealmextensions.*
import io.realm.CollectionUtils.copyToRealm
import io.realm.Realm
import io.realm.internal.ObjectServerFacade
import io.realm.kotlin.where
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Container.ffetch(barcode: String?, containerBarcode: String = ""): Container? {

    if(barcode.isNullOrEmpty()) return null // MARK: Если код транспортной упаковки отсутствует, возвращаем NULL
    var container = Container().fetch(barcode)  // MARK: Ищем запись по коду транспортной упаковки или создаем ее

    var containerRef: Container?
    if(container == null){
        Container(barcode = barcode).save()
        containerRef = Container().queryLast()
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