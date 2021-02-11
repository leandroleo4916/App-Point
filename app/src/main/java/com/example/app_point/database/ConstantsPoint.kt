package com.example.app_point.database

class ConstantsPoint private constructor(){

    object POINT {
        const val TABLE_NAME = "point"
        object COLUMNS {
            const val ID = "id"
            const val EMPLOYEE = "name"
            const val HOUR = "hora"
            const val DATE = "data"
        }
    }
}