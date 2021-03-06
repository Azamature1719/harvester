package com.example.harvester.model.entities.realm_extensions

import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel

/**
 * Realm configuration store per class
 * Created by magillus on 8/14/2017.
 */
class RealmConfigStore {
    companion object {
        var TAG = RealmConfigStore::class.java.simpleName
        private var configMap: MutableMap<Class<out RealmModel>, RealmConfiguration> = HashMap()

        /**
         * Initialize realm configuration for class
         */
        fun <T : RealmModel> init(modelClass: Class<T>, realmCfg: RealmConfiguration) {
            Log.d(TAG, "Adding class $modelClass to realm ${realmCfg.realmFileName}")
            if (!configMap.containsKey(modelClass)) {
                configMap.put(modelClass, realmCfg)
            }
        }

        /**
         * Fetches realm configuration for class.
         */
        fun <T : RealmModel> fetchConfiguration(modelClass: Class<T>): RealmConfiguration? {
            return configMap[modelClass]
        }
    }
}

fun <T : RealmModel> T.getRealmInstance(): Realm {
    return RealmConfigStore.fetchConfiguration(this::class.java)?.realm() ?: Realm.getDefaultInstance()
}

fun RealmConfiguration.realm(): Realm {
    return Realm.getInstance(this)
}