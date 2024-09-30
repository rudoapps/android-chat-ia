package es.rudo.chatia.core.utils

import androidx.compose.ui.graphics.Color
import android.graphics.Color as graphicsColor

fun String.toColorFromHex() : Color? {
    val parsedValue = try {
        Color(graphicsColor.parseColor(this))
    } catch(e: Exception) {
        null
    }
    return parsedValue
}