package com.example.harvester.framework.ui.main

import androidx.lifecycle.*
import com.example.harvester.framework.ui.state.AppState
import com.example.harvester.model.api.TableOfGoodsAPI
import com.example.harvester.model.api.WebServiceResponse
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType
import com.example.harvester.model.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel (private val repository: Repository)
    : ViewModel(), LifecycleObserver {

    private var liveDataToObserve = MutableLiveData<AppState>()
    private lateinit var tableOfGoodsAPI: TableOfGoodsAPI
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.137.1:15085/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // MARK: Получить объект LiveData
    fun getLiveData() = liveDataToObserve

    // MARK: Получить таблицу товаров от веб-сервера
    fun downloadTable(){
        tableOfGoodsAPI = retrofit.create(TableOfGoodsAPI::class.java)
        val webServiceRequest: Call<WebServiceResponse> = tableOfGoodsAPI.fetchTable(id="0000-0008")
        webServiceRequest.enqueue(object : Callback<WebServiceResponse> {
            override fun onFailure(call: Call<WebServiceResponse>, t: Throwable) {
                liveDataToObserve.postValue(AppState.ErrorOccured("Произошла ошибка при получении таблицы от сервера"))
            }
            override fun onResponse(call: Call<WebServiceResponse>, response: Response<WebServiceResponse>) {
                val webServiceResponse: WebServiceResponse? = response.body()
                liveDataToObserve.postValue(AppState.TableDownloaded(webServiceResponse!!.table))
            }
        })
    }

    // MARK: Сохранить данные о товарах в базе данных
    fun fillDatabaseWith(table: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.fillDatabase(table)
            liveDataToObserve.postValue(AppState.DatabaseFilled)
        }
    }

    // MARK: Удалить данные из базы данных
    fun deleteTable(){
        if(repository.isEmpty())
            liveDataToObserve.postValue(AppState.NoData)
        else{
            repository.clearDatabase()
            liveDataToObserve.postValue(AppState.TableDeleted)
        }
    }

    fun modeIsNone(): Boolean {
        return repository.getProcessingMode() == ProcessingModeType.none
    }

    // Установить режим работы
    fun setProcessingMode(){
        when(repository.getProcessingMode()){
            ProcessingModeType.collection ->{
                liveDataToObserve.postValue(AppState.Collection(repository.getProductsFromDatabase()))
            }
            ProcessingModeType.revision ->{
                liveDataToObserve.postValue(AppState.Revision(repository.getProductsFromDatabase()))
            }
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