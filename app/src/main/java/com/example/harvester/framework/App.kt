package com.example.harvester.framework

import android.app.Application
import android.content.Context
import com.example.harvester.di.appModule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    fun initRealm() {
        Realm.init(this@App)
        val config = RealmConfiguration.Builder()
            .name("harvester_realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }
}