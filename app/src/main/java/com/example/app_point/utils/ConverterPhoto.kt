package com.example.app_point.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

class ConverterPhoto {

    fun converterToByteArray(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val sizeImage = scaleDown(bitmap, 250.toFloat(), true)
        val stream = ByteArrayOutputStream()
        sizeImage!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun converterToBitmap(image: ByteArray): Bitmap{
        val photo = ByteArrayInputStream(image)
        return BitmapFactory.decodeStream(photo)
    }

    private fun scaleDown(realImage: Bitmap, maxImageSize: Float, filter: Boolean): Bitmap? {
        val ratio = (maxImageSize / realImage.width).coerceAtMost(maxImageSize / realImage.height)
        val width = (ratio * realImage.width).roundToInt()
        val height = (ratio * realImage.height).roundToInt()
        return Bitmap.createScaledBitmap(realImage, width, height, filter)
    }

}