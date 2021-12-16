package com.example.app_point.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.entity.Employee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.EmployeeNameAndPhoto

class RepositoryEmployee(private val mDataBaseEmployee: DataBaseEmployee) {

    fun conditionEmployee(employee: EmployeeEntity): String {
        return if (employee.id == 0) { saveEmployee(employee) }
        else { editEmployee(employee) }
    }

    private fun saveEmployee(employee: EmployeeEntity): String {

        return try {
            val db = mDataBaseEmployee.writableDatabase
            val insertValues = ContentValues()
            insertValues.run {
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO, employee.photo)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE, employee.active)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION, employee.vacation)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1, employee.horario1)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2, employee.horario2)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3, employee.horario3)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4, employee.horario4)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD, employee.workload)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, employee.nameEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO, employee.cargoEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL, employee.emailEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE, employee.phoneEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION, employee.admissaoEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO, employee.aniversarioEmployee)
            }

            db.insert(ConstantsEmployee.EMPLOYEE.TABLE_NAME, null, insertValues)
            "salvo"

        } catch (e: Exception) {
            "não salvo"
        }
    }

    private fun editEmployee(employee: EmployeeEntity): String {

        return try {
            val db = mDataBaseEmployee.writableDatabase
            val projection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(employee.id.toString())

            val contentValues = ContentValues()
            contentValues.run {
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ID, employee.id)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE, employee.active)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION, employee.vacation)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO, employee.photo)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1, employee.horario1)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2, employee.horario2)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3, employee.horario3)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4, employee.horario4)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME, employee.nameEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO, employee.cargoEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL, employee.emailEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE, employee.phoneEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION, employee.admissaoEmployee)
                put(ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO, employee.aniversarioEmployee)
            }

            db.update(ConstantsEmployee.EMPLOYEE.TABLE_NAME, contentValues, projection, args)
            "editado"

        } catch (e: Exception) {
            "não editado"
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
                ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4,
                ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD,
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
                    val id = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                    val active = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE))
                    val vacation = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION))
                    val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                    val hora1 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1))
                    val hora2 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2))
                    val hora3 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3))
                    val hora4 = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4))
                    val work = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD))
                    val name = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                    val email = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL))
                    val cargo = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                    val phone = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE))
                    val admissao = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))
                    val niver = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO))

                    listData.add(EmployeeEntity(id, active, vacation, photo, hora1, hora2, hora3,
                        hora4, work, name, email, cargo, phone, admissao, niver)
                    )
                }
            }
            cursor?.close()
            return listData

        } catch (e: Exception) {
            return listData
        }
    }

    fun consultEmployeeNameAndPhoto(): ArrayList<EmployeeNameAndPhoto> {

        val listData: ArrayList<EmployeeNameAndPhoto> = arrayListOf()
        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME)

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
                    val id = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                    val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                    val name = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))

                    listData.add(EmployeeNameAndPhoto(id, photo,  name)
                    )
                }
            }
            cursor?.close()
            return listData

        } catch (e: Exception) {
            return listData
        }
    }

    fun consultEmployee(): ArrayList<Employee> {

        val listData: ArrayList<Employee> = arrayListOf()
        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION)

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
                    val id = cursor.getInt(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                    val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                    val name = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                    val cargo = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                    val admission = cursor.getString(cursor.getColumnIndex(
                        ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))

                    listData.add(Employee(id, photo, name, cargo, admission))
                }
            }
            cursor?.close()
            return listData

        } catch (e: Exception) {
            return listData
        }
    }

    fun consultEmployee(nome: String): EmployeeEntity {

        var listData: EmployeeEntity? = null
        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4,
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
                val id = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                val active = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE))
                val vacation = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION))
                val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                val hora1 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1))
                val hora2 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2))
                val hora3 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3))
                val hora4 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4))
                val work = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD))
                val name = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                val email = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL))
                val cargo = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                val phone = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE))
                val admissao = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))
                val niver = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO))

                listData = EmployeeEntity(id, active, vacation, photo, hora1, hora2, hora3,
                    hora4, work, name, email, cargo, phone, admissao, niver)

            }
            cursor?.close()
            return listData!!

        } catch (e: Exception) {
            return listData!!
        }
    }

    fun consultDataEmployeeId(id: Int): EmployeeEntity? {

        var employee: EmployeeEntity? = null
        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(
                ConstantsEmployee.EMPLOYEE.COLUMNS.ID,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3,
                ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4,
                ConstantsEmployee.EMPLOYEE.COLUMNS.NAME,
                ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL,
                ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO,
                ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION,
                ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO)

            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME, projection,
                selection, args, null, null, null)

            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()
                val idEmployee = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
                val active = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ACTIVE))
                val vacation = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.VACATION))
                val photo: ByteArray = cursor.getBlob(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))
                val hora1 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO1))
                val hora2 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO2))
                val hora3 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO3))
                val hora4 = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.HORARIO4))
                val work = cursor.getInt(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD))
                val name = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.NAME))
                val email = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.EMAIL))
                val cargo = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.CARGO))
                val phone = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.PHONE))
                val admissao = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ADMISSION))
                val niver = cursor.getString(cursor.getColumnIndex(
                    ConstantsEmployee.EMPLOYEE.COLUMNS.ANIVERSARIO))

                employee = EmployeeEntity(idEmployee, active, vacation, photo, hora1, hora2, hora3,
                    hora4, work, name, email, cargo, phone, admissao, niver)

            }
            cursor?.close()
            return employee!!

        } catch (e: Exception) {
            return employee!!
        }
    }

    fun removeEmployee(id: Int): Boolean {

        return try {
            val db = mDataBaseEmployee.writableDatabase
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            db.delete(ConstantsEmployee.EMPLOYEE.TABLE_NAME, selection, args)
            true

        } catch (e: Exception) { false }
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

                return cursor.getBlob(
                    cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.PHOTO))

            }
            cursor?.close()
            return null

        } catch (e: Exception) {
            return null
        }
    }

    fun consultTime(name: String): Int? {

        try {
            val db = mDataBaseEmployee.readableDatabase
            val projection = arrayOf(ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD)
            val selection = ConstantsEmployee.EMPLOYEE.COLUMNS.NAME + " = ?"
            val args = arrayOf(name)

            val cursor = db.query(
                ConstantsEmployee.EMPLOYEE.TABLE_NAME, projection, selection, args, null,
                null, null
            )
            if (cursor != null && cursor.count > 0) {
                cursor.moveToNext()
                return cursor.getInt(cursor.getColumnIndex(ConstantsEmployee.EMPLOYEE.COLUMNS.WORKLOAD))
            }
            cursor?.close()
            return null

        } catch (e: Exception) {
            return null
        }
    }

}

