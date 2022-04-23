package com.example.harvester.model.repository

import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.DTO.XMLRecordDTO

interface Repository {
    fun clear();
    fun fillDatabase(tableOfGoods: String)
    fun getProductsFromDatabase(): MutableList<ProductInfoDTO>
    fun getProductsFromTable(tableOfGoods: String):MutableList<XMLRecordDTO>
    fun fullFillDatabase(records: MutableList<XMLRecordDTO>)
}