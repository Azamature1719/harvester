package com.example.harvester.model.entities.realm_entities.classifier_object.product

import com.example.harvester.framework.App
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.product_type.ProductType
import io.realm.Realm
import io.realm.kotlin.where
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

    val fetchedProduct = fetch(description)
    if(fetchedProduct != null)
        return fetchedProduct

    var article = record.article
    if(article.isNullOrEmpty()) article = ""

    var markedGoodTypeCode = record.markedGoodTypeCode
    if(markedGoodTypeCode == 0 || markedGoodTypeCode == null)
        markedGoodTypeCode = ProductType.none.ordinal

    var product: Product = Product()
    product.description = description
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
    var productRef = App.realm.copyToRealm(product)
    App.realm.commitTransaction()
    return productRef
}

fun Product.fetch(description: String): Product?{
    val product = App.realm.where(Product::class.java).equalTo("description", description).findFirstAsync()
    return product
}
