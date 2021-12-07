package com.example.app_point.utils

import com.example.app_point.R

class GetColor {

    fun retColor (position: Int): Int{

        return when(position%10){
               0 -> R.drawable.back_fundo_blue
               1 -> R.drawable.back_fundo_blue2
               2 -> R.drawable.back_fundo_orange
               3 -> R.drawable.back_fundo_pink
               4 -> R.drawable.back_fundo_green
               5 -> R.drawable.back_fundo_blue
               6 -> R.drawable.back_fundo_blue2
               7 -> R.drawable.back_fundo_orange
               8 -> R.drawable.back_fundo_pink
            else -> R.drawable.back_fundo_green
        }
    }
}