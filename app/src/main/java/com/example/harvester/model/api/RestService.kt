package com.example.harvester.model.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.harvester.model.DTO.WebServiceResponse
import com.example.harvester.model.entities.TableOfGoodsXML
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private val TAG = "HarvesterWebService"

class RestService {
    private val tableOfGoodsAPI: TableOfGoodsAPI

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.63:15085/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        tableOfGoodsAPI = retrofit.create(TableOfGoodsAPI::class.java)
    }

    fun fetchTableOfGoods(): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val flickrRequest: Call<WebServiceResponse> = tableOfGoodsAPI.fetchTable(id="0000-0001")

        flickrRequest.enqueue(object : Callback<WebServiceResponse> {
            override fun onFailure(call: Call<WebServiceResponse>, t: Throwable) {
                Log.e(TAG, "Ошибка получения таблицы товаров", t)
            }

            override fun onResponse(
                call: Call<WebServiceResponse>,
                response: Response<WebServiceResponse>
            ) {
                val webServiceResponse: WebServiceResponse? = response.body()
                Log.d(TAG, webServiceResponse!!.table)
                responseLiveData.postValue(webServiceResponse!!.table)
            }
        })

        return responseLiveData
    }
}