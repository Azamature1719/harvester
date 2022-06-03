package com.example.harvester.framework.ui.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.harvester.R
import com.google.android.gms.common.internal.Preconditions


class ViewFinderOverlay(context: Context, attrs: AttributeSet) : View(context, attrs) {

//    private val boxPaint: Paint = Paint().apply {
//        color = ContextCompat.getColor(context, R.color.barcode_reticle_stroke)
//        style = Paint.Style.STROKE
//        strokeWidth = context.resources.getDimensionPixelOffset(R.dimen.barcode_reticle_stroke_width).toFloat()
//    }

    private val qrCodeZonePaint: Paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.teal_200)
        style = Paint.Style.STROKE
        strokeWidth = context.resources.getDimensionPixelOffset(R.dimen.barcode_reticle_stroke_width).toFloat()
    }

//    private val scrimPaint: Paint = Paint().apply {
//        color = ContextCompat.getColor(context, R.color.barcode_reticle_background)
//    }
//
//    private val eraserPaint: Paint = Paint().apply {
//        strokeWidth = boxPaint.strokeWidth
//        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
//    }

    private val boxCornerRadius: Float =
        context.resources.getDimensionPixelOffset(R.dimen.barcode_reticle_corner_radius).toFloat()

    private var scaleFactor = 1.0f
    private var lock = Any()
    private var imageHeight: Int = 0
    private var imageWidth: Int = 0
    private var isFlipped: Boolean = false
    private var needUpdateTransformation = true
    private var postScaleWidthOffset = 1f
    private var postScaleHeightOffset = 0f

    private fun setImageSourceInfo(imageWidth: Int, imageHeight: Int) {
        Preconditions.checkState(imageWidth > 0, "image width must be positive")
        Preconditions.checkState(imageHeight > 0, "image height must be positive")
        synchronized(lock) {
            this.imageWidth = imageWidth
            this.imageHeight = imageHeight
            needUpdateTransformation = true
        }
    }

    fun setViewFinder() {
        val overlayWidth = width.toFloat()
        val overlayHeight = height.toFloat()
        val boxWidth = overlayWidth * 80 / 100
        val boxHeight = overlayHeight * 36 / 100
        val cx = overlayWidth / 2
        val cy = overlayHeight / 2

        // boxRect = RectF(cx - boxWidth / 2, cy - boxHeight / 2, cx + boxWidth / 2, cy + boxHeight / 2)
        boxRect = RectF(cx - boxWidth / 2, cy - boxHeight / 2, cx + boxWidth / 2, cy + boxHeight / 2)

        // invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
//        boxRect.let {
//            // Draws the dark background scrim and leaves the box area clear.
//            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), scrimPaint)
//            // As the stroke is always centered, so erase twice with FILL and STROKE respectively to clear
//            // all area that the box rect would occupy.
//            eraserPaint.style = Paint.Style.FILL
//            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, eraserPaint)
//            eraserPaint.style = Paint.Style.STROKE
//            // canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, eraserPaint)
//            // Draws the box.
//            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, boxPaint)
//        }

        qrCodeZone?.let {
            canvas.drawRoundRect(it, boxCornerRadius, boxCornerRadius, qrCodeZonePaint)
        }
    }

    private fun updateTransformationIfNeeded() {
        if (!needUpdateTransformation || imageWidth <= 0 || imageHeight <= 0) {
            return
        }
        val viewAspectRatio = width.toFloat() / height
        val imageAspectRatio = imageWidth.toFloat() / imageHeight
        postScaleWidthOffset = 0f
        postScaleHeightOffset = 0f
        if (viewAspectRatio > imageAspectRatio) {
            // The image needs to be vertically cropped to be displayed in this view.
            scaleFactor = width.toFloat() / imageWidth
            postScaleHeightOffset = (width.toFloat() / imageAspectRatio - height) / 2
        } else {
            // The image needs to be horizontally cropped to be displayed in this view.
            scaleFactor = height.toFloat() / imageHeight
            postScaleWidthOffset = (height.toFloat() * imageAspectRatio - width) / 2
        }
        needUpdateTransformation = false
    }

    private fun scale(imagePixel: Float): Float {
        return imagePixel * scaleFactor
    }

    private fun translateX(x: Float): Float {
        return if (isFlipped) {
            width - (scale(x) - postScaleWidthOffset)
        } else {
            scale(x) - postScaleWidthOffset
        }
    }

    private fun translateY(y: Float): Float {
        return scale(y) - postScaleHeightOffset
    }

    fun drawQRCodeZone(rect: RectF, w: Int, h: Int, isFlipped : Boolean) {
        this.isFlipped = isFlipped

        setImageSourceInfo(w, h)
        updateTransformationIfNeeded()

        // If the image is flipped, the left will be translated to right, and the right to left.
        val x0 = translateX(rect.left)
        val x1 = translateX(rect.right)

        rect.left = Math.min(x0, x1)
        rect.right = Math.max(x0, x1)

        rect.top = translateY(rect.top)
        rect.bottom = translateY(rect.bottom)
        qrCodeZone = rect

//        if (!boxRect.contains(rect )) {
//            qrCodeZone = null
//        }
        invalidate()
    }


    fun clearCodeZone() {
        qrCodeZone = RectF(0f, 0f, 0f, 0f)
    }


    companion object {
        var boxRect: RectF = RectF()
        var qrCodeZone: RectF? = RectF()
    }
}