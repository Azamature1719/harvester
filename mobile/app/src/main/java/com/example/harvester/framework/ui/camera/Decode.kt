package com.example.harvester.framework.ui.camera

enum class Decode {
    none,
    ean13,
    datamatrix;

    var barcodeExtra: String = ""
    var isGS1Datamatrix: Boolean = false;

    fun gtin(): String {
        if (!this.isGS1Datamatrix) { return "" }
        val startIndex = if (this.barcodeExtra.subSequence(0, 2).equals("01")) 2 else 4
        val endIndex = if (this.barcodeExtra.subSequence(0, 2).equals("01")) 16 else 18

        println("SUBSEQUENCE")
        println(barcodeExtra.subSequence(startIndex, endIndex))

        return this.barcodeExtra.subSequence(startIndex, endIndex).toString()
    }

    fun ean13(): String {
        if (!this.isGS1Datamatrix) { return "" }
        val startIndex = if (this.barcodeExtra.subSequence(1, 3).equals("01")) 3 else 5
        return this.barcodeExtra.subSequence(startIndex, 13).toString()
    }

    companion object {

        open fun init(barcodeExtra: String): Decode {

            if(barcodeExtra.length == 13) {
                var result = ean13
                result.barcodeExtra = barcodeExtra
                return result
            }

//            println(barcodeExtra.subSequence(1,3))
//            println(barcodeExtra.subSequence(16,18))
//            println(barcodeExtra.subSequence(0,4))
//            println(barcodeExtra.subSequence(18,22))

            if((barcodeExtra.subSequence(0,2) == "01" && barcodeExtra.subSequence(16, 18) == "21")
                || (barcodeExtra.subSequence(0,4) == "(01)" && barcodeExtra.subSequence(18, 22) == "(21)")) {

                var result = datamatrix
                result.barcodeExtra = barcodeExtra
                result.isGS1Datamatrix = true;
                println(result.gtin())
                return result
            }

            return none
        }
    }
}