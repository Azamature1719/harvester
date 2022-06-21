package com.example.harvester.framework.ui.state

import com.example.harvester.model.DTO.ProductInfoDTO

sealed class AppState {
    object NoData: AppState()
    data class TableDownloaded(val table: String): AppState()
    object TableDeleted: AppState()
    object DatabaseFilled: AppState()
    data class Revision(val listOfProducts: MutableList<ProductInfoDTO>): AppState()
    data class Collection(val listOfProducts: MutableList<ProductInfoDTO>): AppState()
    data class DocumentSent(val message: String): AppState()
    object DocumentCleaned: AppState()
    data class ErrorOccured(val error: String = "Ошибка"): AppState()
}