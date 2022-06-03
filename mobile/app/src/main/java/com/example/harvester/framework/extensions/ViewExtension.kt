package com.example.harvester.framework.extensions

import android.graphics.drawable.GradientDrawable
import android.view.View

fun View.cornerBackground(color: Int, radius: Int) {
    val gradientDrawable = GradientDrawable()
    gradientDrawable.cornerRadius = radius.toFloat()
    gradientDrawable.setColor(color)

    this.background = gradientDrawable
}