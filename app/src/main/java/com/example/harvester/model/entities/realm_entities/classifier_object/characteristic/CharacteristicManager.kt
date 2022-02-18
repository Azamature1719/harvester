package com.example.harvester.model.entities.realm_entities.classifier_object.characteristic

import com.example.harvester.framework.App
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import io.realm.CollectionUtils.copyToRealm
import io.realm.Realm
import io.realm.kotlin.where

fun Characteristic.ffetch(record: XMLRecordDTO, product: Product?): Characteristic?{
    if(product == null) return null

    var desc = record.characteristicOfNomenclature
    if (desc.isNullOrEmpty()) return null
//
//    var previousChar = Characteristic().fetch(desc)
//    if(previousChar != null) return previousChar

    var characteristic = Characteristic()
    characteristic.desc = desc
    characteristic.product = product


    App.realm.beginTransaction()
    var characteristicRef = App.realm.copyToRealm(characteristic)
    App.realm.commitTransaction()
    return characteristicRef
}

fun Characteristic.fetch(desc: String): Characteristic?{
    val characteristicQuery = App.realm.where(Characteristic::class.java)
    return characteristicQuery.like("desc", desc).findFirst()
}