package com.example.harvester.model.entities.realm_entities.classifier_object.characteristic

import com.example.harvester.framework.App
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.CollectionUtils.copyToRealm
import io.realm.Realm
import io.realm.kotlin.where

fun Characteristic.ffetch(record: XMLRecordDTO, product: Product?): Characteristic?{
    if(product == null) return null

    var description = record.characteristicOfNomenclature
    if (description.isNullOrEmpty()) return null

    var previousChar = Characteristic().fetch(description)
    if(previousChar != null) return previousChar

    var characteristic = Characteristic()
    characteristic.description = description
    characteristic.product =

    App.realm.beginTransaction()
    var charRef = App.realm.copyToRealm(characteristic)
    App.realm.commitTransaction()

    return charRef
}

fun Characteristic.fetch(description: String): Characteristic?{
    return App.realm.where(Characteristic::class.java).equalTo("description", description).findFirst()
}