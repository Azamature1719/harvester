package com.example.harvester.framework

import android.app.Application
import android.content.Context
import com.example.harvester.di.appModule
import com.example.harvester.model.entities.realm_entities.information_register.identification.Identification
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingDocument
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType
import com.example.harvester.model.entities.realm_extensions.queryLast
import com.example.harvester.model.entities.realm_extensions.save
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initRealm()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
    private fun initRealm() {
        Realm.init(this@App)
        val config = RealmConfiguration.Builder()
            .name("Harvester.realm")
            .initialData (){
                it.createObject(ProcessingDocument::class.java, UUID.randomUUID().toString())
                it.createObject(Identification::class.java, UUID.randomUUID().toString())
            }
            .build()
        Realm.setDefaultConfiguration(config)
    }
}