package com.example.harvester.model.entities.realm_entities.classifier_object.characteristic

import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.example.harvester.model.entities.realm_extensions.deleteAll
import com.example.harvester.model.entities.realm_extensions.queryAll
import com.example.harvester.model.entities.realm_extensions.queryFirst
import com.example.harvester.model.entities.realm_extensions.save

fun Characteristic.ffetch(record: XMLRecordDTO, product: Product?): Characteristic?{
    if(product == null) return null

    var description = record.characteristicOfNomenclature
    if (description.isNullOrEmpty()) return null

    var previousChar = Characteristic().fetch(description)
    if(previousChar != null) return previousChar

    return Characteristic(description = description, product = product).save()
}

fun Characteristic.fetch(description: String): Characteristic?{
    return Characteristic().queryFirst{equalTo("description", description)}
}

fun Characteristic.findAll(): List<Characteristic> {
    return Characteristic().queryAll()
}

fun Characteristic.clear(){
    Characteristic().deleteAll()
}