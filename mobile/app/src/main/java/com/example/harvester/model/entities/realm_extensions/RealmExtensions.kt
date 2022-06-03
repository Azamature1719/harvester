package com.example.harvester.model.entities.realm_extensions

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmQuery
import java.lang.reflect.Field

typealias Query<T> = RealmQuery<T>.() -> Unit

@Retention(AnnotationRetention.RUNTIME)
annotation class AutoIncrementPK

/**
 * Query to the database with RealmQuery instance as argument and returns all items founded
 */
fun <T : RealmModel> T.queryAll(): List<T> {
    getRealmInstance().use { realm ->
        val result = realm.where(this.javaClass).findAll()
        return realm.copyFromRealm(result)
    }
}

/**
 * Query to the database with RealmQuery instance as argument. Return first result, or null.
 */
fun <T : RealmModel> T.queryFirst(query: Query<T>): T? {
    getRealmInstance().use { realm ->
        val item: T? = realm.where(this.javaClass).withQuery(query).findFirst()
        return if (item != null && RealmObject.isValid(item)) realm.copyFromRealm(item) else null
    }
}

/**
 * Query to the database with RealmQuery instance as argument. Return last result, or null.
 */
fun <T : RealmModel> T.queryLast(): T? {
    getRealmInstance().use { realm ->
        val result = realm.where(this.javaClass).findAll()
        return if (result != null && result.isNotEmpty()) realm.copyFromRealm(result.last()) else null
    }
}

/**
 * Utility extension for modifying database. Create a transaction, run the function passed as argument,
 * commit transaction and close realm instance.
 */
fun Realm.transaction(action: (Realm) -> Unit) {
    use {
        if (!isInTransaction) {
            executeTransaction { action(this) }
        } else {
            action(this)
        }
    }
}

/**
 * Creates a new entry in database or updates an existing one. If entity has no primary key, always create a new one.
 * If has primary key, it tries to updates an existing one.
 */
fun <T : RealmModel> T.save() : T? {
    getRealmInstance().transaction { realm ->
        if (isAutoIncrementPK()) {
            initPk(realm)
        }
        if (this.hasPrimaryKey(realm))
            realm.copyToRealmOrUpdate(this)
        else
            realm.copyToRealm(this)
    }
    return this.queryLast()
}


/**
 * Delete all entries of this type in database
 */
fun <T : RealmModel> T.deleteAll() {
    getRealmInstance().transaction { it.where(this.javaClass).findAll().deleteAllFromRealm() }
}

/**
 * Delete all entries returned by the specified query
 */
fun <T : RealmModel> T.delete(myQuery: Query<T>) {
    getRealmInstance().transaction {
        it.where(this.javaClass).withQuery(myQuery).findAll().deleteAllFromRealm()
    }
}

/**
 * Get count of entries
 */
fun <T : RealmModel> T.count(): Long {
    getRealmInstance().use { realm ->
        return realm.where(this::class.java).count()
    }
}

/**
 * UTILITY METHODS
 */
private fun <T> T.withQuery(block: (T) -> Unit): T {
    block(this); return this
}

/**
 * UTILITY METHODS
 */
fun <T : RealmModel> T.hasPrimaryKey(realm: Realm): Boolean {
    if (realm.schema.get(this.javaClass.simpleName) == null) {
        throw IllegalArgumentException(this.javaClass.simpleName + " is not part of the schema for this Realm. Did you added realm-android plugin in your build.gradle file?")
    }
    return realm.schema.get(this.javaClass.simpleName)?.hasPrimaryKey() ?: false
}

inline fun <reified T : RealmModel> T.getLastPk(realm: Realm): Long {
    getPrimaryKeyFieldName(realm).let { fieldName ->
        val result = realm.where(this.javaClass).max(fieldName)
        return result?.toLong() ?: 0
    }
}

inline fun <reified T : RealmModel> T.getPrimaryKeyFieldName(realm: Realm): String? {
    return realm.schema.get(this.javaClass.simpleName)?.primaryKey
}

inline fun <reified T : RealmModel> T.setPk(realm: Realm, value: Long) {
    getPrimaryKeyFieldName(realm).let { fieldName ->
        val f1 = javaClass.getDeclaredField(fieldName)
        try {
            val accesible = f1.isAccessible
            f1.isAccessible = true
            if (f1.isNullFor(this)) {
                //We only set pk value if it does not have any value previously
                f1.set(this, value)
            }
            f1.isAccessible = accesible
        } catch (ex: IllegalArgumentException) {
            throw IllegalArgumentException("Primary key field $fieldName must be of type Long to set a primary key automatically")
        }
    }
}

fun RealmModel.initPk(realm: Realm) {
    setPk(realm, getLastPk(realm) + 1)
}

fun <T : RealmModel> T.isAutoIncrementPK(): Boolean {
    return this.javaClass.declaredAnnotations.any { it.annotationClass == AutoIncrementPK::class }
}

fun Field.isNullFor(obj: Any) = try {
    get(obj) == null
} catch (ex: NullPointerException) {
    true
}