package com.example.harvester.model.repository

import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType

interface Repository {
    fun isEmpty(): Boolean
    fun getProductsFromDatabase(): MutableList<ProductInfoDTO>
    fun fillDatabase(tableOfGoods: String)
    fun fullFillDatabase(records: MutableList<XMLRecordDTO>)
    fun parseTable(tableOfGoods: String):MutableList<XMLRecordDTO>
    fun clearDatabase();
    fun clearDocumentCollection();
    fun clearDocumentRevision();
    fun getWebServiceAddress(): String
    fun getDeviceID(): String
    fun setWebServiceAddress(webServiceAddress: String)
    fun setDeviceID(deviceID: String)
    fun getProcessingStatus(): ProcessingStatusType
    fun getProcessingMode(): ProcessingModeType
    fun setCollection()
    fun setRevision()
    fun setNone()
    fun makeXML(): String
}
