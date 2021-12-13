package com.example.app_point.interfaces

import android.view.View
import com.google.android.material.snackbar.Snackbar

interface IHideNavView {
    fun hideNavView (value: Boolean){}
    fun snackBar(child: View, snackBar: Snackbar.SnackbarLayout)
}