package com.example.harvester.model.DTO

import com.example.harvester.model.entities.realm_entities.product_type.ProductType

data class XMLRecordDTO(
    var barCode: String? = null,
    var barCodeBase64: String? = null,
    var name: String? = null,
    var article: String? = null,
    var unitOfMeasurement: String? = null,
    var characteristicOfNomenclature: String? = null,
    var seriesOfNomenclature: String? = null,
    var quality: String? = null,
    var price: Double = 0.0,
    var quantity: Int = 0,
    var containerBarcode: String? = null,
    var containerBarcodeBase64: String? = null,
    var markedGoodTypeCode: Int = ProductType.none.ordinal,
    var alcohol: Boolean = false,
    var alcoholExcisable: Boolean? = null,
    var alcoholKindCode: String? = null,
    var alcoholCode: String? = null,
    var alcoholContainerSize: Double? = null,
    var alcoholStrength: Double? = null,
    var alcoholExciseStamp: String? = null,
    var alcoholExciseStampBase64: String? = null,
)
