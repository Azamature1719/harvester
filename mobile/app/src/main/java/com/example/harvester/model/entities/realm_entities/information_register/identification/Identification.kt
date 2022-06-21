package com.example.harvester.model.entities.realm_entities.information_register.identification
import com.example.harvester.model.entities.realm_extensions.save
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Identification: RealmObject() {
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()

    private var _webServiceAddress: String = "http://127.0.0.1:15085"

    var webServiceAddress: String
    get() = _webServiceAddress
    set(value) {
        _webServiceAddress = value
        save()
    }

    private var _deviceID: String = "0000-0010"

    var deviceID: String
    get() = _deviceID
    set(value) {
        _deviceID = value
        save()
    }
}