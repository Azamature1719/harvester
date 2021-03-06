package com.example.harvester.model.repository

import com.example.harvester.model.DTO.ProductInfoDTO
import com.example.harvester.model.DTO.XMLRecordDTO
import com.example.harvester.model.parsing.XMLParser
import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.Characteristic
import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.clear
import com.example.harvester.model.entities.realm_entities.classifier_object.characteristic.ffetch
import com.example.harvester.model.entities.realm_entities.classifier_object.product.*
import com.example.harvester.model.entities.realm_entities.information_register.barcode.Barcode
import com.example.harvester.model.entities.realm_entities.information_register.barcode.clear
import com.example.harvester.model.entities.realm_entities.information_register.barcode.ffetch
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.BarcodeHarvested
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.clear
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.update
import com.example.harvester.model.entities.realm_entities.information_register.container.Container
import com.example.harvester.model.entities.realm_entities.information_register.container.clear
import com.example.harvester.model.entities.realm_entities.information_register.container.ffetch
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.DataHarvested
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.clear
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.findAll
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.update
import com.example.harvester.model.entities.realm_entities.information_register.identification.Identification
import com.example.harvester.model.entities.realm_entities.information_register.price.Price
import com.example.harvester.model.entities.realm_entities.information_register.price.clear
import com.example.harvester.model.entities.realm_entities.information_register.price.fetch
import com.example.harvester.model.entities.realm_entities.information_register.price.update
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingDocument
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingModeType
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType
import com.example.harvester.model.entities.realm_entities.product_type.ProductType
import com.example.harvester.model.entities.realm_extensions.count
import com.example.harvester.model.entities.realm_extensions.queryLast
import com.example.harvester.model.entities.realm_extensions.save
import kotlinx.coroutines.*

class RepositoryImpl: Repository {

    // MARK: Заполнить базу данных из переданной XML-таблицы товаров
    override fun fillDatabase(tableOfGoods: String) {
        fullFillDatabase(parseTable(tableOfGoods))
    }

    // MARK: Получить товары из базы данных
    override fun getProductsFromDatabase(): MutableList<ProductInfoDTO> {
        val dataHarvested: List<DataHarvested> = DataHarvested().findAll()
        var listOfProducts: MutableList<ProductInfoDTO> = mutableListOf()
        for (i in dataHarvested.indices){
            val dataHarvestedProduct = dataHarvested[i].product
            val dataHarvestedCharacteristic = dataHarvested[i].characteristic
            val dataHarvestedPrice = Price().fetch(dataHarvestedProduct, dataHarvestedCharacteristic)
            var curProduct = ProductInfoDTO()
            curProduct.alcoholCapacity = dataHarvestedProduct?.alcoholCapacity
            curProduct.alcoholCode = dataHarvestedProduct?.alcoholCode
            curProduct.alcoholVolume = dataHarvestedProduct?.alcoholVolume
            curProduct.article = dataHarvestedProduct?.article
            curProduct.markedGoodTypeCode = dataHarvestedProduct?._marked ?: ProductType.none.ordinal
            curProduct.name = dataHarvestedProduct?.description
            curProduct.description = dataHarvestedCharacteristic?.description
            curProduct.price = dataHarvestedPrice?.price ?: 0.0
            curProduct.quantity = dataHarvested[i]?.quantity.toInt()
            curProduct.quantityAcc = dataHarvested[i]?.quantityAcc.toInt()
            listOfProducts.add(curProduct)
        }
        return listOfProducts
    }

    // MARK: Распарсить XML-таблицу товаров
    override fun parseTable(tableOfGoods: String): MutableList<XMLRecordDTO> {
        return XMLParser.parseXML(tableOfGoods)
    }

    // MARK: Заполнить базу данных товарами из распарсенной таблицы товаров
    override fun fullFillDatabase(records: MutableList<XMLRecordDTO>) {
        var current_mode: ProcessingModeType = ProcessingModeType.collection
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
            if (containerBarcode.isNullOrEmpty())
                containerBarcode = " "
            if (!containerBarcodeBase64.isNullOrEmpty())
                containerBarcode = containerBarcodeBase64

            // -- Сохранение товаров в таблицу ContainerRecordManager --
            val container = Container().ffetch(containerBarcode)
            val product = Product().ffetch(record)
            val characteristic = Characteristic().ffetch(record, product)
            val barcodeRecord = Barcode().ffetch(barcode, product, characteristic)

            var price: Double? = record.price
            if (price != null)
                if (price > 0.0)
                    Price().update(price.toDouble(), product, characteristic)

            var quantity: Int? = record.quantity

            // Quantity == 0 -> Запись в DHRM / BHRM не производится
            if (quantity != null) {
                var desc: String = " "
                if(quantity > 0)
                    desc = barcode
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
        if(DataHarvested().count() > 0)
            setRevision()
        else
            setCollection()
    }

    override fun makeXML(): String = XMLParser.makeXML()


    override fun clearDocumentCollection() {
        CoroutineScope(Dispatchers.IO).launch {
            BarcodeHarvested().clear()
            DataHarvested().clear()
        }
    }

    override fun clearDocumentRevision() {
        CoroutineScope(Dispatchers.IO).launch {
            BarcodeHarvested().clear()
            for(dataHarvested in DataHarvested().findAll()){
                dataHarvested.quantity = 0.0
                dataHarvested.save()
            }
        }
    }

    // MARK: Удаление всех данных из базы данных
    override fun clearDatabase(){

        runBlocking {
            withContext(Dispatchers.IO){
                Product().clear()
                Characteristic().clear()
                Barcode().clear()
                BarcodeHarvested().clear()
                Container().clear()
                DataHarvested().clear()
                Price().clear()
            }
        }
    }

    /// MARK: Проверить, что в базе есть данные о продуктах
    override fun isEmpty(): Boolean =
        (Product().count() == 0L)

    // MARK: Получить режим заполнения документа
    override fun getProcessingMode() =
        ProcessingDocument().queryLast()!!.mode

    // MARK: Получить статус заполнения документа
    override fun getProcessingStatus() =
        ProcessingDocument().queryLast()!!.state

    // MARK: Установить режим Сбора товаров
    override fun setCollection(){
        runBlocking {
            withContext(Dispatchers.IO){
                ProcessingDocument().queryLast()!!.state = ProcessingStatusType.processing
                ProcessingDocument().queryLast()!!.mode = ProcessingModeType.collection
            }
        }
    }

    // MARK: Установить режим Сверки товаров
    override fun setRevision(){
        runBlocking {
            withContext(Dispatchers.IO){
                ProcessingDocument().queryLast()!!.state = ProcessingStatusType.processing
                ProcessingDocument().queryLast()!!.mode = ProcessingModeType.revision
            }
        }
    }

    // MARK: Установить режим Харвестер (Начальный)
    override fun setNone(){
        runBlocking {
            withContext(Dispatchers.IO){
                ProcessingDocument().queryLast()!!.state = ProcessingStatusType.finished
                ProcessingDocument().queryLast()!!.mode = ProcessingModeType.none
            }
        }
    }
    // MARK: Получить хост веб-сервиса
    override fun getWebServiceAddress() = Identification().queryLast()!!.webServiceAddress

    // MARK: Получить идентификатор устройства
    override fun getDeviceID() = Identification().queryLast()!!.deviceID

    // MARK: Установить адрес веб-сервиса
    override fun setWebServiceAddress(webServiceAddress: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Identification().queryLast()!!.webServiceAddress = webServiceAddress
        }
    }

    // MARK: Установить идентификатор устройства
    override fun setDeviceID(deviceID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Identification().queryLast()!!.deviceID = deviceID
        }
    }
}