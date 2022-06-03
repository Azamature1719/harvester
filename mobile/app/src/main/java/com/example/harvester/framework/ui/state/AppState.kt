package com.example.harvester.framework.ui.state

import com.example.harvester.model.DTO.ProductInfoDTO

sealed class AppState {
    object NoData: AppState() // данных в базе нет
    object ProductsUploaded: AppState()
    object ProductsDeleted: AppState()
    data class Revision(val listOfProducts: MutableList<ProductInfoDTO>): AppState()
    data class Collection(val listOfProducts: MutableList<ProductInfoDTO>): AppState()
    object DocumentSent: AppState()
    object DocumentCleaned: AppState()
}