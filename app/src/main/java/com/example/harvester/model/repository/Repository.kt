package com.example.harvester.model.repository

import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.DTO.XMLRecordDTO
import io.realm.Realm
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getProductsFromDatabase(): MutableList<ProductInfoDTO>
    fun getProductsFromXMLTable():MutableList<XMLRecordDTO>
    fun fullFillDatabase(records: MutableList<XMLRecordDTO>)
}