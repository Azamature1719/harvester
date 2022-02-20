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
        databaseInit(context = this)
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }


    companion object {
        lateinit var realm: Realm

        fun databaseInit(context: Context) {
            Realm.init(context)
            val config = RealmConfiguration.Builder().name("harvester_realm")
                .deleteRealmIfMigrationNeeded()
                .build()
            realm = Realm.getInstance(config)
            Realm.setDefaultConfiguration(config)
        }
    }
}