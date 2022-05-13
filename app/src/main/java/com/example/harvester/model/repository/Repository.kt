package com.example.harvester.model.repository

import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType

interface Repository {
    fun fillDatabase(tableOfGoods: String)
    fun getProductsFromDatabase(): MutableList<ProductInfoDTO>
    fun parseTable(tableOfGoods: String):MutableList<XMLRecordDTO>
    fun fullFillDatabase(records: MutableList<XMLRecordDTO>)
    fun clearDatabase();
    fun isEmpty(): Boolean
    fun getProcessingStatus(): ProcessingStatusType
    fun getProcessingMode(): ProcessingModeType
    fun setCollection()
    fun setRevision()
    fun setNone()
}