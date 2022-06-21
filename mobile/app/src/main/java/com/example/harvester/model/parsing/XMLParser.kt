package com.example.harvester.model.parsing

import android.util.Base64
import android.util.Xml
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.BarcodeHarvested
import com.example.harvester.model.entities.realm_extensions.queryAll
import io.realm.Realm
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import org.xmlpull.v1.XmlSerializer
import java.io.StringReader

object XMLParser {
    private var factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
    private var parser: XmlPullParser = factory.newPullParser()

    fun makeXML(): String {
        var barcodeHarvested = Realm.getDefaultInstance().use {
            var query = it.where(BarcodeHarvested::class.java)
            val result = query.findAll()
            return@use it.copyFromRealm(result)
        }
        val xmlSerializer = Xml.newSerializer()
        return xmlSerializer.document {
            element("Table"){
                barcodeHarvested.forEach{
                    if(it.quantity != 0.0){
                        element("Record"){
                            var barcodeBase64 = Base64.encodeToString(it.barcode!!.barcode.toByteArray(), Base64.NO_WRAP)
                            attribute("BarCodeBase64", barcodeBase64)
                            attribute("Quantity", it.quantity.toString())
                        }
                    }
                }
            }
        }
    }

    fun parseXML(tableOfGoods: String): MutableList<XMLRecordDTO> {
        parser.setInput(StringReader(tableOfGoods))
        var listOfRecords = mutableListOf<XMLRecordDTO>()
        while(parser.eventType != XmlPullParser.END_DOCUMENT){
            if(parser.name == "Record"){ // Чтобы не рассматривать тег Table
                when (parser.eventType){
                    XmlPullParser.START_TAG -> {
                        var recordDTO: XMLRecordDTO = XMLRecordDTO()
                        for (i in 0 until parser.attributeCount) {
                            val attributeValue = parser.getAttributeValue(i)
                            when(parser.getAttributeName(i)) {
                                "BarCode" -> recordDTO.barCode = attributeValue
                                "BarCodeBase64" -> recordDTO.barCodeBase64 = attributeValue
                                "Name" -> recordDTO.name = attributeValue
                                "Article" -> recordDTO.article = attributeValue
                                "UnitOfMeasurement" -> recordDTO.unitOfMeasurement = attributeValue
                                "CharacteristicOfNomenclature" -> recordDTO.characteristicOfNomenclature = attributeValue
                                "SeriesOfNomenclature" -> recordDTO.seriesOfNomenclature = attributeValue
                                "Quality" -> recordDTO.quality = attributeValue
                                "Price" -> recordDTO.price = attributeValue.toDouble()
                                "Quantity" -> recordDTO.quantity = attributeValue.toInt()
                                "ContainerBarcode" -> recordDTO.containerBarcode = attributeValue
                                "ContainerBarcodeBase64" -> recordDTO.containerBarcodeBase64 = attributeValue
                                "MarkedGoodTypeCode" -> recordDTO.markedGoodTypeCode = attributeValue.toInt()
                                "Alcohol" -> recordDTO.alcohol = attributeValue.toBoolean()
                                "AlcoholExcisable" -> recordDTO.alcoholExcisable = attributeValue.toBoolean()
                                "AlcoholKindCode" -> recordDTO.alcoholKindCode = attributeValue
                                "AlcoholCode" -> recordDTO.alcoholCode = attributeValue
                                "AlcoholContainerSize" -> recordDTO.alcoholContainerSize = attributeValue.toDouble()
                                "AlcoholStrength" -> recordDTO.alcoholStrength = attributeValue.toDouble()
                                "AlcoholExciseStamp" -> recordDTO.alcoholExciseStamp = attributeValue
                                "AlcoholExciseStampBase64" -> recordDTO.alcoholExciseStampBase64 = attributeValue
                            }
                        }
                        listOfRecords.add(recordDTO)
                    }
                }
            }
            parser.next()
        }
        return listOfRecords
    }

}