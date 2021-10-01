package com.example.app_point.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.entity.EmployeeEntity

class RepositoryEmployee(context: Context?) {

    private val mDataBaseEmployee: DataBaseEmployee = DataBaseEmployee(context)

    fun conditionEmployee(employee: EmployeeEntity): String{

        return if (employee.id == 0){
            setEmployee(employee)
        }else{
            editEmployee(employee)
        }

    }

    private fun setEmployee(employee: EmployeeEntity): String {

        return try{
            val db = mDataBaseEmployee.writableDatabase
            val insertValues = ContentValues()
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO, employee.photo)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1, employee.horario1)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2, employee.horario2)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3, employee.horario3)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4, employee.horario4)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, employee.nameEmployee)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO, employee.cargoEmployee)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL, employee.emailEmployee)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE, employee.phoneEmployee)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION, employee.admissaoEmployee)
            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO, employee.aniversarioEmployee)

            db.insert(ConstantsEmployee.EMPLOYEE.TABLE_NAME, null, insertValues)
            "salvo"

        } catch (e: Exception){
            "não salvo"
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

    fun consultFullEmployee(): ArrayList<EmployeeEntity> {

        val listData: ArrayList<EmployeeEntity> = arrayListOf()
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

            val cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
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

                    listData.add(
                        EmployeeEntity(id, photo, hora1, hora2, hora3, hora4, name, email, cargo, phone, admissao, niver)
                    )
                }
            }
            cursor?.close()
            return listData

        } catch (e: Exception) {
            return listData
        }
    }

    fun consultDataEmployee(nome: String): EmployeeEntity? {

        var listData: EmployeeEntity? = null
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

                    listData = EmployeeEntity(idEmployee, photo, hora1, hora2, hora3, hora4, name, email, cargo, phone, admissao, niver)

            }
            cursor?.close()
            return listData!!

        } catch (e: Exception) {
            return listData!!
        }
    }

    fun consultIdEmployee(nome: String): Int {

        val id = 0
        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.ID)

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
                return cursor.getInt(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.ID))

            }
            cursor?.close()
            return id

        } catch (e: Exception) {
            return id
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

    private fun editEmployee(employee: EmployeeEntity): String {

        return try{
            val db = mDataBaseEmployee.writableDatabase
            val projection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(employee.id.toString())

            val contentValues = ContentValues()
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, employee.id)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO, employee.photo)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1, employee.horario1)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2, employee.horario2)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3, employee.horario3)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4, employee.horario4)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, employee.nameEmployee)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO, employee.cargoEmployee)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL, employee.emailEmployee)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE, employee.phoneEmployee)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION, employee.admissaoEmployee)
            contentValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO, employee.aniversarioEmployee)

            db.update(ConstantsEmployee.EMPLOYEE.TABLE_NAME, contentValues, projection, args)
            "editado"

        } catch (e: Exception){
            "não editado"
        }
    }

    fun removeEmployee(name: String): Boolean {

        return try{
            val db = mDataBaseEmployee.writableDatabase
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(name)

            db.delete(ConstantsEmployee.EMPLOYEE.TABLE_NAME, selection, args)
            true

        } catch (e: Exception){
            false
        }
    }

    fun consultPhoto(nome: String): ByteArray? {

        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO)
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

                return cursor.getBlob(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))

            }
            cursor?.close()
            return null

        } catch (e: Exception) {
            return null
        }
    }

    fun consultHours(nome: String): ArrayList<String>? {

        var list: ArrayList<String>? = arrayListOf()

        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(nome)

            val cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME, projection, selection, args, null,
                null, null
            )
            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()
                val hora1 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO1))
                val hora2 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO2))
                val hora3 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO3))
                val hora4 = cursor.getString(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.HOURARIO4))

                list = arrayListOf(hora1, hora2, hora3, hora4)

            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

}

