package com.example.app_point.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.createDialog(message: String): AlertDialog.Builder {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(message)
    builder.setCancelable(false)
    return builder
}
