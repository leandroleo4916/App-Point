package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_point.Constants.ConstantsEmployee
import com.example.app_point.Constants.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.utils.EmployeeEntity

class RepositoryEmployee(context: Context?) {

    private val mDataBaseEmployee: DataBaseEmployee = DataBaseEmployee(context)

    fun conditionEmployee(id: Int, photo: ByteArray, hour1: String, hour2: String, hour3: String, hour4: String, name: String,
                          cargo: String, email: String, phone: String, admissao: String, aniversario: String): Boolean{

        return if (id == 0){
            getEmployee(photo, hour1, hour2, hour3, hour4, name, cargo, email, phone,
                admissao, aniversario)
        }else{
            editEmployee(id, photo, hour1, hour2, hour3, hour4, name, cargo, email, phone,
                admissao, aniversario)
        }

    }

    fun getEmployee(photo: ByteArray, hour1: String, hour2: String, hour3: String, hour4: String, name: String,
                    cargo: String, email: String, phone: String, admissao: String, aniversario: String): Boolean {

        return try{
            val db = mDataBaseEmployee.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO, photo)
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

    fun consultDadosEmployee(nome: String): EmployeeEntity? {

        var listDados: EmployeeEntity? = null
        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME,
                ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO)

            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            val cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME,
                projection,
                selection,
                args,
                null,
                null,
                null
            )

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()
                    val idEmployee = cursor.getInt(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                    val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                    val hora1 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1))
                    val hora2 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2))
                    val hora3 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3))
                    val hora4 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4))
                    val name = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                    val email = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL))
                    val cargo = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                    val phone = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE))
                    val admissao = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))
                    val niver = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO))

                    listDados = EmployeeEntity(idEmployee, photo, hora1, hora2, hora3, hora4, name, email, cargo, phone, admissao, niver)

            }
            cursor?.close()
            return listDados!!

        } catch (e: Exception) {
            return listDados!!
        }
    }

    fun consultDadosEmployeeId(id: Int): EmployeeEntity? {

        var listDados: EmployeeEntity? = null
        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME,
                ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO)

            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME,
                projection,
                selection,
                args,
                null,
                null,
                null
            )

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()
                val idEmployee = cursor.getInt(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                val hora1 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1))
                val hora2 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2))
                val hora3 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3))
                val hora4 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4))
                val name = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                val email = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL))
                val cargo = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                val phone = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE))
                val admissao = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))
                val niver = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO))

                listDados = EmployeeEntity(idEmployee, photo, hora1, hora2, hora3, hora4, name, email, cargo, phone, admissao, niver)

            }
            cursor?.close()
            return listDados!!

        } catch (e: Exception) {
            return listDados!!
        }
    }

    fun editEmployee(id: Int, photo: ByteArray, hour1: String, hour2: String, hour3: String, hour4: String, name: String,
                    cargo: String, email: String, phone: String, admissao: String, aniversario: String): Boolean {

        return try{
            val db = mDataBaseEmployee.writableDatabase
            val projection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val contentValues = ContentValues()
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, id)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO, photo)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1, hour1)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2, hour2)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3, hour3)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4, hour4)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, name)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO, cargo)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL, email)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE, phone)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION, admissao)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO, aniversario)

            db.update(ConstantsEmployee.EMPLOYEE.TABLE_NAME, contentValues, projection, args)
            true

        } catch (e: Exception){
            false
        }
    }

}

