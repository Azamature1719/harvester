package com.example.harvester.framework.ui.main

import androidx.lifecycle.*
import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.api.RestService
import com.example.harvester.model.api.TableOfGoodsAPI
import com.example.harvester.model.entities.TableOfGoodsXML
import com.example.harvester.model.repository.Repository

class MainViewModel (private val repository: Repository) : ViewModel(), LifecycleObserver {
    private var liveDataToObserve = MutableLiveData<Any>()

    fun getLiveData() = liveDataToObserve
    fun getProducts() = getDataFromLocalSource()
    fun fillDatabase(tableOfGoods: String) = fillDatabaseWithProducts(tableOfGoods)
    fun clearDataBase() {
        repository.clear()
    }
    private fun getDataFromWebService() {
        liveDataToObserve.postValue(RestService().fetchTableOfGoods())
    }
    private fun fillDatabaseWithProducts(tableOfGoods: String) {
        repository.fillDatabase(tableOfGoods)
    }
    private fun getDataFromLocalSource() {
        fillDatabaseWithProducts(TableOfGoodsXML.fullTable)
        liveDataToObserve.postValue(repository.getProductsFromDatabase())
    }
}