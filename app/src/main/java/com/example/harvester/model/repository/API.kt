//package com.example.harvester.model.repository
//
//import android.content.Context
//import com.android.volley.Request
//import com.android.volley.RequestQueue
//import com.android.volley.toolbox.Volley
//import kotlinx.serialization.Serializable
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//
//// Обработка JSON-объектов метода PING
//@Serializable
//data class PingMethod (val PING1C: String)
//@Serializable
//data class PingResponse(val success: Boolean, val message: String?)
//
//// Обработка JSON-объектов метода GET
//@Serializable
//data class GetMethodContent (val TYPE: String, val DEVICEID: String)
//@Serializable
//data class GetMethod(val GET1C: GetMethodContent)
//@Serializable
//data class GetResponse(val success: Boolean, val message: String)
//
//class API {
//
//    private val url =  "http://api.egaisblack.ru//api//v3"
//    private var bodyGET: String = "{\"GET1C\":{\"TYPE\":\"GOODSTABLE\", \"DEVICEID\":\"AAAA\"}}"
//    private var bodyPOST: String = "{\"POST1C\":{ \"TYPE\":\"DOWNLOADTABLE\", \"DEVICEID\":\"AAAA\"}}"
//    private var bodyPING: String = "{\"PING1C\":\" \"}"
//
//    private val co = context
//
//    private val jsonPING: String = Json.encodeToString(PingMethod(" "))
//
//    // TODO: Придумать, куда убрать формирование JSON
//    private val getContent = GetMethodContent("GOODSTABLE", "AAAA")
//    private val getMethod = GetMethod(getContent)
//    private val jsonGET: String = Json.encodeToString(getMethod)
//
//    companion object{
//        @Volatile
//        private var INSTANCE: NetworkSingleton? = null
//        fun getInstance(context: Context) =
//            INSTANCE ?: synchronized(this) {
//                INSTANCE ?: NetworkSingleton(context).also {
//                    INSTANCE = it
//                }
//            }
//    }
//
//    private val requestQueue: RequestQueue by lazy {
//        Volley.newRequestQueue(context.applicationContext)
//    }
//
//    private fun <T> addToRequestQueue(req: Request<T>) {
//        requestQueue.add(req)
//
//    }
//
//    private fun findBody(method: String): String {
//        return when(method){
//            "GET" -> jsonGET
//            "POST" -> bodyPOST
//            "PING" -> jsonPING
//            else -> " "
//        }
//    }
//
//    private fun findBodyLength(method: String): Int{
//        return when(method){
//            "GET" -> jsonGET.length
//            "POST" -> bodyPOST.length
//            "PING" -> jsonPING.length
//            else -> 0
//        }
//    }
//}