package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_point.database.ConstantsEmployee
import com.example.app_point.database.DataBaseEmployee

class RepositoryEmployee(context: Context?) {

    private val mDataBaseEmployee: DataBaseEmployee = DataBaseEmployee(context)

    fun getEmployee(hour1: String, hour2: String, hour3: String, hour4: String, name: String,
                    cargo: String, email: String, phone: String, admissao: String,
                    aniversario: String): Boolean {

        return try{
            val db = mDataBaseEmployee.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1, hour1)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2, hour2)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3, hour3)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4, hour4)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, name)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO, cargo)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL, email)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE, phone)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION, admissao)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO, aniversario)

            db.insert(ConstantsEmployee.EMPLOYEE.TABLE_NAME, null, insertValues)
            true

        } catch (e: Exception){
            false
        }
    }

    fun employeeList(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()

        try {
            val cursor: Cursor
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME)
            val orderBy = ConstantsEmployee.EMPLOYEE.COLUMNS.ID

            cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                orderBy
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val nomeEmployee =
                        cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))

                    list.add(nomeEmployee)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun getOfficeList(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()

        try {
            val cursor: Cursor
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO)
            val orderBy = ConstantsEmployee.EMPLOYEE.COLUMNS.ID

            cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                orderBy
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val officeEmployee =
                        cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))

                    list.add(officeEmployee)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }
}

