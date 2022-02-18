package com.example.harvester.model.entities

import com.example.harvester.model.entities.realm_entities.product_type.ProductType
import java.util.*

object TableOfGoodsXML {
    val emptyTable: String = "<Table FullLoad=\"true\">\n" +
            "    <Record BarCodeBase64=\"MjAwMDAwMDAwMjA4OA==\" Name=\"Ботинки мужские демисезонные\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"40, Зеленый, 6, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"10\" Quantity=\"0\" Article=\"123-456\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MjAwMDAwMDAwMDEzOA==\" Name=\"Ботинки женские демисезонные\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"упак (10 пар)\" CharacteristicOfNomenclature=\"43, Бежевый, 5, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"20\" Quantity=\"0\" Article=\"456-789\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjU=\" Name=\"Ботинки Gucci Huyucci 12-345\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"35, Жёлтый, 7, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"30\" Quantity=\"0\" Article=\"789-012\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjA=\" Name=\"Ботинки Intielec Wref 17-56-1-2\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"36, Белый, 6, искусственная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"40\" Quantity=\"0\" Article=\"012-345\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjB=\" Name=\"Сапоги Intielec cok-cok-cok\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"41, Черный, 23, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"50\" Quantity=\"0\" Article=\"345-678\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjC=\" Name=\"Водочка Беленькая 0.75\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Очищенная ООО Путинка 2000-2036\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"60\" Quantity=\"0\" Article=\"678-901\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"200\" AlcoholContainerSize=\"0.75\" AlcoholStrength=\"40\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1a\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjD=\" Name=\"Вино ЛЕ ГРАНД НУАР\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Шардоне белое сухое (Франция), 0.75\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"70\" Quantity=\"0\" Article=\"901-234\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"300\" AlcoholContainerSize=\"0.5\" AlcoholStrength=\"30\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1b\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjE=\" Name=\"Вино ВАЙН ГАЙД\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Изабелла, красное полусладкое, 0.75\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"80\" Quantity=\"0\" Article=\"234-567\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"220\" AlcoholContainerSize=\"2.0\" AlcoholStrength=\"20\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1c\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjF=\" Name=\"Коньяк армянский АЙВАЗОВСКИЙ, 5 лет\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"40 %, 0.5л ОАО Вина Армении\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"90\" Quantity=\"0\" Article=\"567-890\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"100\" AlcoholContainerSize=\"0.25\" AlcoholStrength=\"10\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1d\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjG=\" Name=\"Напиток винный ЛАВЕТТИ\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Классико, белый, сладкий 0.75\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"100\" Quantity=\"0\" Article=\"890-123\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"900\" AlcoholContainerSize=\"1.0\" AlcoholStrength=\"0\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1e\"/>\n" +
            "</Table>\n"
    val fullTable: String = "<Table FullLoad=\"true\">\n" +
            "    <Record BarCodeBase64=\"MjAwMDAwMDAwMjA4OA==\" Name=\"Ботинки мужские демисезонные\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"40, Зеленый, 6, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"10\" Quantity=\"10\" Article=\"\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MjAwMDAwMDAwMDEzOA==\" Name=\"Ботинки женские демисезонные\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"упак (10 пар)\" CharacteristicOfNomenclature=\"43, Бежевый, 5, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"20\" Quantity=\"10\" Article=\"\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjU=\" Name=\"Ботинки Gucci Huyucci 12-345\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"35, Жёлтый, 7, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"30\" Quantity=\"10\" Article=\"\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjA=\" Name=\"Ботинки Intielec Wref 17-56-1-2\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"36, Белый, 6, искусственная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"40\" Quantity=\"10\" Article=\"\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjB=\" Name=\"Сапоги  Intielec cok-cok-cok\" MarkedGoodTypeCode=\"4\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"41, Черный, 23cm, натуральная кожа\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"50\" Quantity=\"10\" Article=\"\" Alcohol=\"false\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjC=\" Name=\"Водочка Беленькая 0.75\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Очищенная ООО Путинка 2000-2036\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"60\" Quantity=\"10\" Article=\"\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"200\" AlcoholContainerSize=\"0.75\" AlcoholStrength=\"40\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1a\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjD=\" Name=\"Вино ЛЕ ГРАНД НУАР\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Шардоне белое сухое (Франция), 0.75\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"70\" Quantity=\"20\" Article=\"\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"300\" AlcoholContainerSize=\"0.5\" AlcoholStrength=\"30\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1b\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjE=\" Name=\"Вино ВАЙН ГАЙД\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Изабелла, красное полусладкое, 0.75\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"80\" Quantity=\"30\" Article=\"\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"220\" AlcoholContainerSize=\"2.0\" AlcoholStrength=\"20\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1c\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjF=\" Name=\"Коньяк армянский АЙВАЗОВСКИЙ, 5 лет\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"40 %, 0.5л ОАО Вина Армении\" SeriesOfNomenclature=\"\" Quality=\"40\" Price=\"90\" Quantity=\"0\" Article=\"\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"100\" AlcoholContainerSize=\"0.25\" AlcoholStrength=\"10\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1d\"/>\n" +
            "    <Record BarCodeBase64=\"MDUwMjU2MTE0ODUyMjG=\" Name=\"Напиток винный ЛАВЕТТИ\" MarkedGoodTypeCode=\"0\" UnitOfMeasurement=\"\" CharacteristicOfNomenclature=\"Классико, белый, сладкий 0.75\" SeriesOfNomenclature=\"\" Quality=\"\" Price=\"100\" Quantity=\"50\" Article=\"\" Alcohol=\"true\" AlcoholExcisable=\"false\" AlcoholKindCode=\"\" AlcoholCode=\"900\" AlcoholContainerSize=\"1.0\" AlcoholStrength=\"0\" AlcoholExciseStampBase64=\"QWxjb2hvbEV4Y2lzZVN0YW1e\"/>\n" +
            "</Table>"
}

//fun getDefaultShoes(): Product{
//    return Product(
//        uuid = UUID.randomUUID().toString(),
//        code = "200",
//        productName = "Ботинки BROOMAN",
//        description = "36, Белый, 6, Искусственная кожа",
//        article = "092-661-12",
//        volume = null,
//        percentage = null,
//        productType = ProductType.shoes
//    )
//}
//
//fun getDefaultAlcohol(): Product{
//    return Product(
//        uuid = UUID.randomUUID().toString(),
//        code = "1000",
//        productName = "Изабелла ООО КРЫМСКИЕ ВИНА",
//        description = "Вино красное полусладкое 18%",
//        article = "011-556-17",
//        volume = "0.750",
//        percentage = "35",
//        productType = ProductType.alcoholMarked
//    )
//}