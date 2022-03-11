package com.example.harvester.model.entities.realm_entities.classifier_object.product

import com.example.harvester.MainActivity
import com.example.harvester.framework.App
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.product_type.ProductType
import com.vicpin.krealmextensions.*
import io.realm.CollectionUtils.copyToRealm
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.where
import kotlinx.coroutines.*
import kotlin.reflect.typeOf

fun Product.ffetch(record: XMLRecordDTO): Product?{
    var description: String?
    var alcoholCode = record.alcoholCode
    var name = record.name

    if(alcoholCode.isNullOrEmpty())
        description = name
    else
        description = alcoholCode

    var unitMeasurement = record.unitOfMeasurement
    if(description.isNullOrEmpty()) description = unitMeasurement
    if(description.isNullOrEmpty())
        return null

    val fetchedProduct = Product().fetch(description)
    if(fetchedProduct != null)
        return fetchedProduct

    var article = record.article
    if(article.isNullOrEmpty()) article = ""

    var markedGoodTypeCode = record.markedGoodTypeCode
    if(markedGoodTypeCode == 0)
        markedGoodTypeCode = ProductType.none.ordinal

    var product: Product = Product(description = description, article = article)
    if(record.alcohol){
        if(record.alcoholExcisable == null || record.alcoholExcisable == false)
            product._marked = ProductType.alcoholUnMarked.ordinal
        else
            product._marked = ProductType.alcoholMarked.ordinal
        product.alcoholCapacity = record.alcoholStrength!!
        product.alcoholCode = record.alcoholCode.toString()
        product.alcoholVolume = record.alcoholContainerSize!!
    }
    else
        product._marked = markedGoodTypeCode
    product.save()

    return Product().queryLast()
}

fun Product.fetch(description: String): Product?{
    return Product().queryFirst { equalTo("description", description) }
}

fun Product.findAll(): List<Product> {
    return Product().queryAll()
}
