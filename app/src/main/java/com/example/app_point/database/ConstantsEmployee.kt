package com.example.app_point.database

class ConstantsEmployee private constructor(){

    object EMPLOYEE {
        const val TABLE_NAME = "employee"
        object COLUMNS {
            const val ID = "id"
            const val PHOTO = "photo"
            const val NAME = "name"
            const val CARGO = "cargo"
            const val EMAIL = "email"
            const val PHONE = "telefone"
            const val ADMISSION = "admission"
            const val ANIVERSARIO = "aniversario"
            const val HOURARIO1 = "hourario1"
            const val HOURARIO2 = "hourario2"
            const val HOURARIO3 = "hourario3"
            const val HOURARIO4 = "hourario4"
        }
    }
}