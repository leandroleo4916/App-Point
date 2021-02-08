package com.example.app_point.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ConverterPhoto {

    fun converterToByteArray(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun converterToBitmap(image: ByteArray): Bitmap{
        val photo = ByteArrayInputStream(image)
        return BitmapFactory.decodeStream(photo)
    }

}