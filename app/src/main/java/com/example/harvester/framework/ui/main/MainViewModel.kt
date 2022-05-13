package com.example.harvester.framework.ui.main

import androidx.lifecycle.*
import com.example.harvester.framework.ui.state.AppState
import com.example.harvester.model.entities.TableOfGoodsXML
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType
import com.example.harvester.model.repository.Repository

class MainViewModel (private val repository: Repository) : ViewModel(), LifecycleObserver {
    private var liveDataToObserve = MutableLiveData<AppState>()

    // MARK: Получить объект LiveData
    fun getLiveData() = liveDataToObserve

    // MARK: Загрузить товары в базу данных
    fun uploadProducts(){
        repository.fillDatabase(TableOfGoodsXML.fullTable)
        liveDataToObserve.postValue(AppState.ProductsUploaded)
    }

    // MARK: Удалить данные из базы данных
    fun clearDatabase(){
        if(repository.isEmpty())
            liveDataToObserve.postValue(AppState.NoData)
        else{
            repository.clearDatabase()
            liveDataToObserve.postValue(AppState.ProductsDeleted)
        }
    }

    fun modeIsNone(): Boolean {
        return repository.getProcessingMode() == ProcessingModeType.none
    }

    fun getProcessingStatus(): ProcessingStatusType {
        return repository.getProcessingStatus()
    }

    fun getProcessingMode(): ProcessingModeType {
        return repository.getProcessingMode()
    }

    // MARK: Выбран режим сверки товаров
    fun startRevision(){
        if(repository.isEmpty())
            liveDataToObserve.postValue(AppState.NoData)
        else{
            repository.setRevision()
            liveDataToObserve.postValue(AppState.Revision(repository.getProductsFromDatabase()))
        }
    }

    // MARK: Выбран режим сбора товаров
    fun startCollection(){
        if(repository.isEmpty())
            liveDataToObserve.postValue(AppState.NoData)
        else {
            repository.setCollection()
            liveDataToObserve.postValue(AppState.Collection(repository.getProductsFromDatabase()))
        }
    }

    // MARK: Очистить документ
    fun clearDocument(){
        repository.setNone()
        liveDataToObserve.postValue(AppState.DocumentCleaned)
    }

    // MARK: Выгрузить документ
    fun sendDocument(){
        repository.setNone()
        liveDataToObserve.postValue(AppState.DocumentSent)
    }
}