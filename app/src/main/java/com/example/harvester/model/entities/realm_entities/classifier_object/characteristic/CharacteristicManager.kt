package com.example.harvester.model.entities.realm_entities.classifier_object.characteristic

import com.example.harvester.framework.App
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.queryLast
import com.vicpin.krealmextensions.save
import io.realm.CollectionUtils.copyToRealm
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.where
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Characteristic.ffetch(record: XMLRecordDTO, product: Product?): Characteristic?{
    if(product == null) return null

    var description = record.characteristicOfNomenclature
    if (description.isNullOrEmpty()) return null

    var previousChar = Characteristic().fetch(description)
    if(previousChar != null) return previousChar

    Characteristic(description = description, product = product).save()
    return Characteristic().queryLast()
}

fun Characteristic.fetch(description: String): Characteristic?{
    return Characteristic().queryFirst{equalTo("description", description)}
}

fun Characteristic.findAll(): List<Characteristic> {
    return Characteristic().queryAll()
}