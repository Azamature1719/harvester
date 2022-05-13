package com.example.harvester.model.entities.realm_entities.information_register.processing_status

import com.example.harvester.model.entities.realm_extensions.save
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание записи регистра Статус обработки документа --
open class ProcessingDocument: RealmObject() {
    // -- Характеристики объекта класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
    var currentDate: Date = Date()
    private var _state: String = ProcessingStatusType.processing.name
    var state: ProcessingStatusType
        get() { return ProcessingStatusType.valueOf(this._state) }
        set(value) {
            this._state = value.name
            save()
        }

    private var _mode: String = ProcessingModeType.none.name
    var mode: ProcessingModeType
        get() { return ProcessingModeType.valueOf(this._mode) }
        set(value) {
            this._mode = value.name
            save()
        }
}
