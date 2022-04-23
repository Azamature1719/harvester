package com.example.harvester.model.api

import com.example.harvester.model.DTO.WebServiceResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TableOfGoodsAPI {
    @GET("/")
    fun fetchTable(
        @Header("Device-Id") id: String,
    ): Call<WebServiceResponse>

    @POST("/")
    fun saveTable(
        @Body() table: String
    ): Call<String>
}