package com.example.harvester.framework.ui.camera

import com.example.harvester.model.entities.realm_entities.information_register.barcode.Barcode
import com.example.harvester.model.entities.realm_entities.information_register.barcode.fetch
import com.example.harvester.model.entities.realm_entities.information_register.barcode.ffetch
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.BarcodeHarvested
import com.example.harvester.model.entities.realm_entities.information_register.barcode_harvested.fetch
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.DataHarvested
import com.example.harvester.model.entities.realm_entities.information_register.data_harvested.ffetch
import com.example.harvester.model.entities.realm_entities.information_register.processing_status.ProcessingStatusType
import com.example.harvester.model.entities.realm_extensions.save
import com.example.harvester.model.repository.RepositoryImpl

class BarcodeHandler() {

    companion object {
        fun eanProcessing(barcode: String): Any? {
            return null
        }

        private fun datamatrixProcessing(datamatrix: Decode): Any? {

            // MARK: - Проверяем на повторное сканирование данных.
            var barcodeHarvested = BarcodeHarvested().fetch(datamatrix.barcodeExtra)
            if (barcodeHarvested != null && !barcodeHarvested.quantity.equals(0))
                return "Код маркировки уже отсканирован ранее."

            // MARK: - Получим элемент справочника штрихкоды по GTIN или EAN.
            var gtin = Barcode().fetch(datamatrix.gtin())
            if (gtin == null) gtin = Barcode().fetch(datamatrix.ean13())

            // MARK: - Принудительно получим элемент справочника штрихкоды по коду маркировки.
            val barcode = Barcode().ffetch(datamatrix.barcodeExtra, product = gtin?.product, characteristic = gtin?.characteristic)

            // MARK: - Обновляем количество в регистрах Собранные данные и Собранные штрихкоды.
            var dataHarvested: DataHarvested
            if (gtin?.product == null) {
                dataHarvested = DataHarvested().ffetch(datamatrix.gtin())
            } else {
                dataHarvested = DataHarvested().ffetch(gtin.product!!, gtin.characteristic!!, container = null)
            }

            dataHarvested.quantity += 1
            dataHarvested.save()

            barcodeHarvested = BarcodeHarvested()
            barcodeHarvested.owner = dataHarvested
            barcodeHarvested.barcode = barcode
            barcodeHarvested.quantity += 1
            barcodeHarvested.save()

            return dataHarvested
        }

      open fun processingMultiDecodedData(decode: Decode): Any? {
            when(decode) {
                Decode.none -> {
                    return "Тип штрихкода не определен."
                }
                Decode.ean13 -> {
                    return eanProcessing(decode.barcodeExtra)
                }
                Decode.datamatrix -> {
                    if(RepositoryImpl().getProcessingStatus() == ProcessingStatusType.finished) return "Товар не добавлен.\nРабота с документом завершена."
                    if(decode.isGS1Datamatrix) return datamatrixProcessing(decode)
                }
            }
            return "Тип штрихкода не определен."
        }
    }
}