package com.example.harvester.model.repository

import com.example.harvester.framework.App
import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.XMLParser
import com.example.harvester.model.entities.TableOfGoodsXML
import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.ffetch
import com.example.harvester.model.entities.realm_entities.classifier_object.product.Product
import com.example.harvester.model.entities.realm_entities.classifier_object.product.ffetch
import com.example.harvester.model.entities.realm_entities.information_register.barcode.Barcode
import com.example.harvester.model.entities.realm_entities.information_register.barcode.ffetch
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.BarcodeHarvested
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.update
import com.example.harvester.model.entities.realm_entities.information_register.container.Container
import com.example.harvester.model.entities.realm_entities.information_register.container.ffetch
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.DataHarvested
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.update
import com.example.harvester.model.entities.realm_entities.information_register.price.Price
import com.example.harvester.model.entities.realm_entities.information_register.price.update
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.toFlow
import io.realm.kotlin.where
import kotlinx.coroutines.flow.Flow

class RepositoryImpl: Repository {
    override fun getProductsFromDatabase(): MutableList<ProductInfoDTO> {
        fullFillDatabase(getProductsFromXMLTable())

        val products = App.realm.where<Product>().findAll()
        val characteristics = App.realm.where<Characteristic>().findAll()
        var listOfProducts: MutableList<ProductInfoDTO> = mutableListOf()

        println(products.size)
        for (i in 0 until products.size){
            var curProduct: ProductInfoDTO = ProductInfoDTO()
            curProduct.alcoholCapacity = products[i]?.alcoholCapacity
            curProduct.alcoholCode = products[i]?.alcoholCode
            curProduct.alcoholVolume = products[i]?.alcoholVolume
            curProduct.article = products[i]?.article
            curProduct.markedGoodTypeCode = products[i]!!._marked
            curProduct.name = products[i]?.desc
            curProduct.description = characteristics[i]?.desc
          //  val price = App.realm.where<Price>().equalTo("product.uuid", products[i]!!.uuid).findFirst()
            curProduct.price = 0.0
            curProduct.quantity = 10
            listOfProducts.add(curProduct)
        }
        return listOfProducts
    }


    override fun getProductsFromXMLTable(): MutableList<XMLRecordDTO> {
        return XMLParser.parseXML(TableOfGoodsXML.fullTable)
    }

    override fun fullFillDatabase(records: MutableList<XMLRecordDTO>) {
        for (record in records) {
            // -- Извлечение данных из таблицы товаров --
            // -- Штрихкод  --
            val barcodeBase64: String? = record.barCodeBase64
            var barcode: String? = record.barCode
            if (!barcodeBase64.isNullOrEmpty()) barcode = barcodeBase64

            // -- Акцизная марка --
            val alcoholExciseStampBase64: String? = record.alcoholExciseStampBase64
            if (!alcoholExciseStampBase64.isNullOrEmpty()) {
                barcode = alcoholExciseStampBase64
            } else { // Если акцизная марка не передана
                val alcoholExciseStamp: String? = record.alcoholExciseStamp
                if (!alcoholExciseStamp.isNullOrEmpty()) {
                    barcode = alcoholExciseStamp
                }
            }

            // -- Если barcode не существует, выходим из функции, записи о товаре не существует --
            if (barcode.isNullOrEmpty()) return

            // -- Поиск в записи информации о транспортной упаковке containerBarcode --
            var containerBarcode: String? = record.containerBarcode
            val containerBarcodeBase64: String? = record.containerBarcodeBase64
            if (containerBarcode.isNullOrEmpty()) containerBarcode = " "
            if (!containerBarcodeBase64.isNullOrEmpty())
                containerBarcode = containerBarcodeBase64

            // -- Сохранение товаров в таблицу ContainerRecordManager --
            val container = Container().ffetch(barcode = containerBarcode)
            val product = Product().ffetch(record)
            val characteristic = Characteristic().ffetch(record, product)
            val barcodeRecord = Barcode().ffetch(barcode, product, characteristic)

//            var price = record.price
//            if (price > 0.0 || price != null)
//                Price().update(price.toDouble(), product, characteristic)

            var quantity = record.quantity

            // Quantity == 0 -> Запись в DHRM / BHRM не производится
            if (quantity > 0 || quantity != null) {
                val desc = barcode

                // Скорее всего из-за того, что в полях другой тип данных поиск с приведением к строке не работает
                val dataHarvestedRecord = DataHarvested().update(
                    desc,
                    product,
                    characteristic,
                    container,
                    quantity.toDouble()
                )
                if (dataHarvestedRecord != null) {
                    BarcodeHarvested().update(
                        dataHarvestedRecord,
                        barcodeRecord,
                        quantity.toDouble()
                    )
                }
            }
        }
    }
}