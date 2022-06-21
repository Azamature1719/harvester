package com.example.harvester.model.entities.realm_entities.information_register.processing_status

enum class ProcessingModeType(val mode: String) {
    none("Харвестр"),        // -- Неопределенный режим --
    collection("Сбор данных"),// -- Сбор данных. Возможен как при наличии данных о Номенклатуре так и без --
    revision("Сверка")       // -- Сверка по документу (Приемка, ревизия, отгрузка по плановому составу) --
}