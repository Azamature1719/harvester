package com.example.harvester.model.entities.realm_entities.information_register.processing_status

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

// -- Описание записи регистра Статус обработки документа --
open class ProcessingStatus: RealmObject() {

    // -- Характеристики объекта класса --
    @PrimaryKey
    var uuid: String = UUID.randomUUID().toString()
    var currentDate: Date = Date()

    enum class ProcessingStatusType(val status: String){
        processing("ОБРАБАТЫВАЕТСЯ"),
        finished("ЗАВЕРШЁН")
    }

    enum class ProcessingModeType(val mode: String){

        // -- Неопределенный режим --
        none("Харверст"),

        // -- Сбор данных. Возможен как при наличии данных о Номенклатуре так и без --
        collection("Сбор кодов"),

        // -- Контроль цен --
        pricecheck("Контроль цен"),

        // -- Сверка по документу (Приемка, ревизия, отгрузка по плановому составу) --
        reconciliation("Сверка")
    }

    private var _state: String = ProcessingStatusType.processing.name
    var state: ProcessingStatusType
        get() { return ProcessingStatusType.valueOf(this._state) }
        set(value) { this._state = value.name }


    private var _mode: String = ProcessingModeType.none.name
    var mode: ProcessingModeType
        get() { return ProcessingModeType.valueOf(this._mode) }
        set(value) { this._mode = value.name }
}
