package com.example.harvester.framework.ui.main

import androidx.lifecycle.*
import com.example.harvester.framework.ui.state.AppState
import com.example.harvester.model.api.PostRequest
import com.example.harvester.model.api.TableOfGoodsAPI
import com.example.harvester.model.api.WebServiceResponse
import com.example.harvester.model.parsing.TableOfGoodsXML
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.repository.Repository
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel (private val repository: Repository)
    : ViewModel(), LifecycleObserver {

    private var liveDataToObserve = MutableLiveData<AppState>()

    init {
        Realm.getDefaultInstance().addChangeListener {
            setProcessingMode()
        }
    }

    // MARK: Получить объект LiveData
    fun getLiveData() = liveDataToObserve

    // MARK: Получить таблицу товаров от веб-сервера
    fun downloadTable(){
         val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(repository.getWebServiceAddress())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var tableOfGoodsAPI = retrofit.create(TableOfGoodsAPI::class.java)

        val webServiceRequest: Call<WebServiceResponse> = tableOfGoodsAPI.fetchTable(id = getDeviceID())
        webServiceRequest.enqueue(object : Callback<WebServiceResponse> {
            override fun onFailure(call: Call<WebServiceResponse>, t: Throwable) {
               // liveDataToObserve.postValue(AppState.ErrorOccured("Произошла ошибка при получении таблицы от сервера"))
            }
            override fun onResponse(call: Call<WebServiceResponse>, response: Response<WebServiceResponse>) {
                val webServiceResponse: WebServiceResponse? = response.body()
                liveDataToObserve.postValue(AppState.TableDownloaded(webServiceResponse!!.table))
            }
        })
    }

    // MARK: Выгрузить документ
    fun sendDocument(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(repository.getWebServiceAddress())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var tableOfGoodsAPI = retrofit.create(TableOfGoodsAPI::class.java)

        //println("{'table':'${repository.makeXML()}'}")
        val json = JsonObject()
        json.addProperty("table", repository.makeXML())

        val webServiceRequest: Call<PostRequest> = tableOfGoodsAPI.saveTable(
            id = getDeviceID(),
            table = json
        )

        webServiceRequest.enqueue(object : Callback<PostRequest> {
            override fun onFailure(call: Call<PostRequest>, t: Throwable) {
               // liveDataToObserve.postValue(AppState.ErrorOccured("Произошла ошибка при отправке таблицы серверу"))
            }
            override fun onResponse(call: Call<PostRequest>, response: Response<PostRequest>) {
                // Удалил отсюда setNone
                liveDataToObserve.postValue(AppState.DocumentSent(response.message()))
            }
        })
    }

    // MARK: Сохранить данные о товарах в базе данных
    fun fillDatabaseWith(table: String){
        CoroutineScope(Dispatchers.IO).launch {
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

    // MARK: Получить идентификатор устройства
    fun getDeviceID(): String = repository.getDeviceID()

    // MARK: Получить адрес веб-сервиса
    fun getWebServiceAddress(): String = repository.getWebServiceAddress()

    // MARK: Установить адрес веб-сервиса
    fun setWebServiceAddress(webServiceAddress: String) =
        repository.setWebServiceAddress(webServiceAddress)

    // MARK: Проверить, установлен ли начальный режим
    fun modeIsNone(): Boolean =
        repository.getProcessingMode() == ProcessingModeType.none

    // MARK: Получить режим работы приложения
    fun getProcessingMode(): ProcessingModeType =
        repository.getProcessingMode()

    // Установить режим работы
    fun setProcessingMode(){
        when(repository.getProcessingMode()){
            ProcessingModeType.collection ->
                liveDataToObserve.postValue(AppState.Collection(repository.getProductsFromDatabase()))
            ProcessingModeType.revision ->
                liveDataToObserve.postValue(AppState.Revision(repository.getProductsFromDatabase()))
          }
    }

    // MARK: Очистить документ в режиме сверки
    fun clearDocumentRevision(){
        // Удалил отсюда setRevision
        repository.clearDocumentRevision()
        liveDataToObserve.postValue(AppState.DocumentCleaned)
    }

    // MARK: Очистить документ в режиме сбора данных
    fun clearDocumentCollection(){
        // Удалил отсюда setCollection
        repository.clearDocumentCollection()
        liveDataToObserve.postValue(AppState.DocumentCleaned)
    }
}