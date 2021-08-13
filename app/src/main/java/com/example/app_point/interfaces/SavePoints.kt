package com.example.app_point.interfaces

interface SavePoints {
    fun savePoints(employee: String, hour: String, date: String): Boolean
}