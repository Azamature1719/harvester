package com.example.harvester.model.entities.realm_entities.classifier_object.product

import com.example.harvester.framework.App
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.product_type.ProductType
import io.realm.Realm
import io.realm.kotlin.where

fun Product.ffetch(record: XMLRecordDTO): Product?{
    var desc: String?
    var alcoholCode = record.alcoholCode
    var name = record.name

    if(alcoholCode.isNullOrEmpty())
        desc = name
    else
        desc = alcoholCode

    var unitMeasurement = record.unitOfMeasurement
    if(desc.isNullOrEmpty()) desc = unitMeasurement

    if(desc.isNullOrEmpty())
        return null
//
//    println(desc)
//    val fetchedProduct = fetch(desc)
//    if(fetchedProduct != null)
//        return fetchedProduct

    var article = record.article
    if(article.isNullOrEmpty()) article = ""

    var markedGoodTypeCode = record.markedGoodTypeCode
    if(markedGoodTypeCode == 0 || markedGoodTypeCode == null)
        markedGoodTypeCode = ProductType.none.ordinal

    var product: Product = Product()
    product.desc = desc
    product.article = article

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


    App.realm.beginTransaction()
    var productRef: Product = App.realm.copyToRealmOrUpdate(product)
    App.realm.commitTransaction()
    return productRef
}

fun Product.fetch(desc: String): Product?{
    val objects = App.realm.where<Product>().equalTo("desc", desc)
    return objects.findFirst()
}
