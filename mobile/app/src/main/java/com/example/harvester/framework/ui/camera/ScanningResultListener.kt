package com.example.harvester.framework.ui.camera

import android.graphics.RectF

interface ScanningResultListener {
    fun onScanned(result: String)
    fun onScannedRect(rect: RectF, w: Int, h: Int, isFlipped: Boolean)
}