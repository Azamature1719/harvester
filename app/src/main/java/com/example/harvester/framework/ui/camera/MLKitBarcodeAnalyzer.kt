package com.example.harvester.framework.ui.camera

import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.toRectF
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MLKitBarcodeAnalyzer(private val listener: ScanningResultListener) : ImageAnalysis.Analyzer {

    private var isScanning: Boolean = true

    private val barcodeScannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_DATA_MATRIX, Barcode.FORMAT_EAN_13, Barcode.FORMAT_QR_CODE)
        .build()

    private val scanner = BarcodeScanning.getClient(barcodeScannerOptions)

    /**
     * This parameters will handle preview box scaling
     */

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image

        if (mediaImage != null && mediaImage.format == ImageFormat.YUV_420_888){ // && isScanning) {
//            val height = mediaImage.height
//            val width = mediaImage.width
//
//            //Coordinate 1
//            val c1x = (width * 0.125).toInt() + 150 //left
//            val c1y = (height * 0.25).toInt() //top
//
//            //Coordinate 2
//            val c2x = (width * 0.875).toInt() - 150 //right
//            val c2y = (height * 0.75).toInt() //bottom
//
//            val rect = Rect(c1x, c1y, c2x, c2y)
////            Log.d("dd--", "Rectangle: $rect")
//
//            val ori: Bitmap = imageProxy.toBitmap()!!
//            val crop = Bitmap.createBitmap(ori, rect.left, rect.top, rect.width(), rect.height())
//
//            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
//
//            val image: InputImage =
//                InputImage.fromBitmap(crop, rotationDegrees)

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees

            scanner.process(image).continueWithTask{
                it.addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    // ...
                    barcodes.firstOrNull().let { barcode ->
                        val rawValue = barcode?.rawValue
                        rawValue?.let {
                            Log.d("Barcode", it)
                            listener.onScanned(it)

                            if (rotationDegrees == 0 || rotationDegrees == 180) {
                                listener.onScannedRect(barcode.boundingBox!!.toRectF(), imageProxy.width, imageProxy.height, true)
                            } else {
                                listener.onScannedRect(barcode.boundingBox!!.toRectF(), imageProxy.height, imageProxy.width, false)
                            }
                        }
                    }
                    imageProxy.close()
                }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                        // isScanning = false
                        imageProxy.close()
                    }.addOnCompleteListener {
                        imageProxy.close()
                    }
            }
//                .addOnSuccessListener { barcodes ->
//                    // Task completed successfully
//                    // ...
//                    barcodes.firstOrNull().let { barcode ->
//                        val rawValue = barcode?.rawValue
//                        rawValue?.let {
//                            Log.d("Barcode", it)
//                            listener.onScanned(it)
//
//                            if (rotationDegrees == 0 || rotationDegrees == 180) {
//                                listener.onScannedRect(barcode.boundingBox!!.toRectF(), imageProxy.width, imageProxy.height, true)
//                            } else {
//                                listener.onScannedRect(barcode.boundingBox!!.toRectF(), imageProxy.height, imageProxy.width, false)
//                            }
//                        }
//                    }
//                    imageProxy.close()
//                }
//                .addOnFailureListener {
//                    // Task failed with an exception
//                    // ...
//                    // isScanning = false
//                    imageProxy.close()
//                }.addOnCompleteListener {
//                    imageProxy.close()
//                }
        }
    }

    fun start() {
        isScanning = true
    }

    fun stop() {
        isScanning = false
    }
}