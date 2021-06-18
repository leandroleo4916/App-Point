package com.example.app_point.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64InputStream
import android.widget.ImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

class ConverterPhoto {

    // Converter photo Bitmap to ByteArray
    fun converterToByteArray(image: ImageView): String? {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val b = stream.toByteArray()
        return Base64.getEncoder().encodeToString(b)
    }

    // Converter photo de ByteArray to Bitmap
    fun converterToBitmap(image: ByteArray): Bitmap{
        val photo = ByteArrayInputStream(image)
        return BitmapFactory.decodeStream(photo)
    }

    // Converter photo String to Bitmap
    fun converterStringToBitmap(image: Any): Bitmap{
        val img = Base64.getDecoder().decode(image.toString())
        val photo = ByteArrayInputStream(img)
        return BitmapFactory.decodeStream(photo)
    }

}