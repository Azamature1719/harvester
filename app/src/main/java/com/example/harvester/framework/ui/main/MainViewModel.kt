package com.example.harvester.framework.ui.main

import androidx.lifecycle.*
import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.repository.Repository
import kotlinx.coroutines.*


class MainViewModel (private val repository: Repository) : ViewModel(), LifecycleObserver {
    private var liveDataToObserve: MutableLiveData<MutableList<ProductInfoDTO>> = MutableLiveData()

    fun getLiveData() = liveDataToObserve

    fun getProducts() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        lateinit var result: MutableList<ProductInfoDTO>
        liveDataToObserve.postValue(repository.getProductsFromDatabase())

    }
}