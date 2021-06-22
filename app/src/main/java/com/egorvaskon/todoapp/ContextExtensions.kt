package com.egorvaskon.todoapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import java.lang.IllegalArgumentException

val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

fun Context.getDrawableCompat(@DrawableRes drawable: Int): Drawable {
    return ContextCompat.getDrawable(this,drawable) ?: throw IllegalArgumentException()
}