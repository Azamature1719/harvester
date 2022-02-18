package com.example.harvester.model.DTO

import com.example.harvester.model.entities.realm_entities.product_type.ProductType

data class ProductInfoDTO (
    var name: String? = null,
    var article: String? = null,
    var description: String? = null,
    var price: Double = 0.0,
    var quantity: Int = 0,
    var markedGoodTypeCode: Int = ProductType.none.ordinal,
    var alcoholCode: String? = null,
    var alcoholVolume: Double? = null,
    var alcoholCapacity: Double? = null,
)

