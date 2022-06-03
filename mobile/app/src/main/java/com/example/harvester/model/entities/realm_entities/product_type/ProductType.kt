package com.example.harvester.model.entities.realm_entities.product_type

// -- Описание типа элемента справочника Номенклатуры --
enum class ProductType (val type: String){
    alcoholMarked("АЛКОГОЛЬ АКЦИЗНЫЙ"),
    alcoholUnMarked ("АЛКОГОЛЬ ПРОЧИЙ"),
    fur ("МЕХОВЫЕ ИЗДЕЛИЯ"),
    tobacco ("ТОБАЧНЫЕ ИЗДЕЛИЯ"),
    shoes ("ОБУВНЫЕ ТОВАРЫ"),
    clothes ("ЛЕГКАЯ ПРОМЫШЛЕННОСТЬ"),
    tires ("ШИНЫ"),
    milk ("МОЛОЧНАЯ ПРОДУКЦИЯ"),
    cameras ("КАМЕРЫ И ВСПЫШКИ"),
    bicycles ("ВЕЛОСИПЕДЫ"),
    wheelchairs ("КРЕСЛА-КОЛЯКИ"),
    perfume ("ДУХИ"),
    sscc ("ТРАНСПОРТНАЯ УПАКОВКА"),
    none ("НЕ МАРКИРОВАННАЯ")
}