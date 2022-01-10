package com.example.app_point.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.constants.ConstantsExtras
import com.example.app_point.constants.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.entity.*
import com.example.app_point.interfaces.RepositoryData
import com.example.app_point.utils.CalculateHours

class RepositoryPoint(private val dataBasePoint: DataBaseEmployee): RepositoryData {

    private val calculateHourExtras = CalculateHours()
    private val repositoryEmployee = RepositoryEmployee(dataBasePoint)

    override fun setPoint(id: Int, employee: String, date: String, hour: String, hourInt: Int): Boolean {

        val searchPoint = selectFullPoints(id, date)

        return try {
            val db = dataBasePoint.writableDatabase
            val projection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)
            val insertValues = ContentValues()

            when {
                searchPoint?.idEmployee == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1, hour)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1INT, hourInt)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE, id)
                    db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
                }
                searchPoint.data != null && searchPoint.hora1 == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1, hour)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1INT, hourInt)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                searchPoint.data != null && searchPoint.hora1 != null && searchPoint.hora2 == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR2, hour)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR2INT, hourInt)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                searchPoint.data != null && searchPoint.hora1 != null && searchPoint.hora2 != null
                        && searchPoint.hora3 == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR3, hour)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR3INT, hourInt)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                searchPoint.data != null && searchPoint.hora1 != null && searchPoint.hora2 != null
                        && searchPoint.hora3 != null && searchPoint.hora4 == null -> {

                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR4, hour)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR4INT, hourInt)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

                    saveHoursExtrasAndPunctuation(id, date)

                }
                searchPoint.hora4 != null -> return false
            }
            true

        } catch (e: Exception) { false }
    }

    private fun saveHoursExtrasAndPunctuation(id: Int, date: String){

        val db = dataBasePoint.writableDatabase
        val projection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
        val args = arrayOf(id.toString(), date)
        val insertValues = ContentValues()

        val pointInt = selectFullPointsInt(id, date)
        val timeEmployee = repositoryEmployee.consultHorarioInAndOut(id)
        val time = repositoryEmployee.consultCargaHoraria(id)
        val extras = calculateHourExtras.calculateHoursExtra(time, HourEntityInt(
            pointInt!!.hora1, pointInt.hora2, pointInt.hora3,
            pointInt.hora4, 0, pointInt.extra))

        val punctuation = calculateHourExtras.punctuation(timeEmployee!!, pointInt)

        insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUREXTRA, extras)
        insertValues.put(ConstantsPoint.POINT.COLUMNS.PUNCTUATION, punctuation)
        db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

        saveTotalBankHoursByEmployee(id, extras)
    }

    private fun saveTotalBankHoursByEmployee(id: Int, extra: Int){

        val consultExtra = consultTotalExtraByIdEmployee(id)

        if (consultExtra == null){
            try {
                val db = dataBasePoint.writableDatabase
                val insertValues = ContentValues()

                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.ID, id)
                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.EXTRA, extra)

                db.insert(ConstantsExtras.EXTRA.TABLE_NAME, null, insertValues)
            }
            catch (e: Exception){ }
        }
        else {

            val totalHour = consultExtra + extra
            try {
                val db = dataBasePoint.writableDatabase
                val projection = ConstantsExtras.EXTRA.COLUMNS.ID + " = ?"
                val args = arrayOf(id.toString())
                val insertValues = ContentValues()

                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.EXTRA, totalHour)

                db.update(ConstantsExtras.EXTRA.TABLE_NAME, insertValues, projection, args)
            }
            catch (e: Exception){ }
        }
    }

    fun consultTotalExtraByIdEmployee(id: Int): Int?{

        var value: Int? = null
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsExtras.EXTRA.COLUMNS.EXTRA)
            val selection = ConstantsExtras.EXTRA.COLUMNS.ID + " = ? "
            val args = arrayOf(id.toString())

            cursor = db.query(
                ConstantsExtras.EXTRA.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    value = cursor?.getInt(cursor.getColumnIndex(ConstantsExtras.EXTRA.COLUMNS.EXTRA))
                }
            }
            cursor?.close()
            return value

        } catch (e: Exception) {
            return value
        }
    }

    override fun setPointExtra(idEmployee: Int, date: String, hour: Int): Boolean {

        return try {
            val db = dataBasePoint.writableDatabase
            val projection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)
            val insertValues = ContentValues()

            insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUREXTRA, hour)
            db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

            true

        } catch (e: Exception) { false }
    }

    override fun setPointByDate (idEmployee: Int, date: String, positionHour: Int,
                                 hour: String, hourInt: Int): Boolean {

        val searchPoint = selectFullPoints(idEmployee, date)

        return try {
            val db = dataBasePoint.writableDatabase
            val projection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)
            val insertValues = ContentValues()

            when (searchPoint?.idEmployee) {
                null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE, idEmployee)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)

                    val valueString = when (positionHour) {
                        1 -> ConstantsPoint.POINT.COLUMNS.HOUR1
                        2 -> ConstantsPoint.POINT.COLUMNS.HOUR2
                        3 -> ConstantsPoint.POINT.COLUMNS.HOUR3
                        else -> ConstantsPoint.POINT.COLUMNS.HOUR4
                    }

                    val valueInt = when (positionHour) {
                        1 -> ConstantsPoint.POINT.COLUMNS.HOUR1INT
                        2 -> ConstantsPoint.POINT.COLUMNS.HOUR2INT
                        3 -> ConstantsPoint.POINT.COLUMNS.HOUR3INT
                        else -> ConstantsPoint.POINT.COLUMNS.HOUR4INT
                    }

                    insertValues.put(valueInt, hourInt)
                    insertValues.put(valueString, hour)

                    db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)

                    return true
                }
                else -> {
                    val valueString = when (positionHour) {
                        1 -> ConstantsPoint.POINT.COLUMNS.HOUR1
                        2 -> ConstantsPoint.POINT.COLUMNS.HOUR2
                        3 -> ConstantsPoint.POINT.COLUMNS.HOUR3
                        else -> ConstantsPoint.POINT.COLUMNS.HOUR4
                    }

                    val valueInt = when (positionHour) {
                        1 -> ConstantsPoint.POINT.COLUMNS.HOUR1INT
                        2 -> ConstantsPoint.POINT.COLUMNS.HOUR2INT
                        3 -> ConstantsPoint.POINT.COLUMNS.HOUR3INT
                        else -> ConstantsPoint.POINT.COLUMNS.HOUR4INT
                    }

                    insertValues.put(valueInt, hourInt)
                    insertValues.put(valueString, hour)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

                    val searchNewPoint = selectFullPoints(idEmployee, date)

                    if (searchNewPoint!!.hora1 != null && searchPoint.hora2 != null &&
                        searchNewPoint.hora3 != null && searchPoint.hora4 != null ){

                        val pointInt = selectFullPointsInt(idEmployee, date)
                        val timeEmployee = repositoryEmployee.consultHorarioInAndOut(idEmployee)
                        val time = repositoryEmployee.consultCargaHoraria(idEmployee)
                        val extras = calculateHourExtras.calculateHoursExtra(time, HourEntityInt(
                                    pointInt!!.hora1, pointInt.hora2, pointInt.hora3,
                                    pointInt.hora4, 0, pointInt.extra))

                        val punctuation = calculateHourExtras.punctuation(timeEmployee!!, pointInt)

                        insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUREXTRA, extras)
                        insertValues.put(ConstantsPoint.POINT.COLUMNS.PUNCTUATION, punctuation)
                        db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

                        saveTotalBankHoursByEmployee(idEmployee, extras)

                        return true
                    }
                }
            }
            true

        } catch (e: Exception) { false }
    }

    override fun selectFullPoints(idEmployee: Int, date: String): PointsEntity? {

        var list: PointsEntity? = null
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.ID))
                    val name = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4))
                    val hourExtra = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list = PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra, idPoint)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun selectFullPointsInt(idEmployee: Int, date: String): HourEntityInt? {

        var list: HourEntityInt? = null
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1INT,
                ConstantsPoint.POINT.COLUMNS.HOUR2INT,
                ConstantsPoint.POINT.COLUMNS.HOUR3INT,
                ConstantsPoint.POINT.COLUMNS.HOUR4INT,
                ConstantsPoint.POINT.COLUMNS.PUNCTUATION,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hour1 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1INT))
                    val hour2 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2INT))
                    val hour3 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3INT))
                    val hour4 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4INT))
                    val punctuation = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.PUNCTUATION))
                    val extra = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    list = HourEntityInt(hour1, hour2, hour3, hour4, punctuation, extra)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun selectPoint(id: Int, date: String): PointsHours? {

        var list: PointsHours? = null
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val data =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))
                    val hourExtra =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    list = PointsHours(data, hour1, hour2, hour3, hour4, hourExtra)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun selectPointInt(id: Int, date: String): HourEntityInt? {

        var list: HourEntityInt? = null
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1INT,
                ConstantsPoint.POINT.COLUMNS.HOUR2INT,
                ConstantsPoint.POINT.COLUMNS.HOUR3INT,
                ConstantsPoint.POINT.COLUMNS.HOUR4INT,
                ConstantsPoint.POINT.COLUMNS.PUNCTUATION,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hour1 =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1INT))
                    val hour2 =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2INT))
                    val hour3 =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3INT))
                    val hour4 =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4INT))
                    val punctuation =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.PUNCTUATION))
                    val extra =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    list = HourEntityInt(hour1, hour2, hour3, hour4, punctuation, extra)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun selectHourExtra(id: Int, date: String): String? {

        return try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.HOUREXTRA)
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                }
            }
            cursor?.close()
            null

        } catch (e: Exception) {
            return null
        }
    }

    override fun fullPoints(): ArrayList<PointsEntity?> {

        val list: ArrayList<PointsEntity?> = arrayListOf()
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.ID))
                    val name = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4))
                    val hourExtra = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra, idPoint))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun fullPointsToName(idEmployee: Int, date: String): ArrayList<PointsEntity?> {

        val list: ArrayList<PointsEntity?> = arrayListOf()

        if (date.isEmpty()) {
            try {
                val cursor: Cursor
                val db = dataBasePoint.readableDatabase
                val projection = arrayOf(
                    ConstantsPoint.POINT.COLUMNS.ID,
                    ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                    ConstantsPoint.POINT.COLUMNS.DATE,
                    ConstantsPoint.POINT.COLUMNS.HOUR1,
                    ConstantsPoint.POINT.COLUMNS.HOUR2,
                    ConstantsPoint.POINT.COLUMNS.HOUR3,
                    ConstantsPoint.POINT.COLUMNS.HOUR4,
                    ConstantsPoint.POINT.COLUMNS.HOUREXTRA,
                    ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
                )
                val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? "
                val args = arrayOf(idEmployee.toString())

                cursor = db.query(
                    ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                    null, null, null
                )

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.ID))
                        val name = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                        val data = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.DATE))
                        val hour1 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR1))
                        val hour2 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR2))
                        val hour3 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR3))
                        val hour4 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR4))
                        val hourExtra = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                        val idPoint = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra, idPoint))
                    }
                }
                cursor?.close()
                return list

            } catch (e: Exception) {
                return list
            }

        } else {
            try {
                val cursor: Cursor
                val db = dataBasePoint.readableDatabase
                val projection = arrayOf(
                    ConstantsPoint.POINT.COLUMNS.ID,
                    ConstantsPoint.POINT.COLUMNS.DATE,
                    ConstantsPoint.POINT.COLUMNS.HOUR1,
                    ConstantsPoint.POINT.COLUMNS.HOUR2,
                    ConstantsPoint.POINT.COLUMNS.HOUR3,
                    ConstantsPoint.POINT.COLUMNS.HOUR4,
                    ConstantsPoint.POINT.COLUMNS.HOUREXTRA,
                    ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
                )
                val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                        ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
                val args = arrayOf(idEmployee.toString(), date)

                cursor = db.query(
                    ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                    null, null, null
                )

                if (cursor != null && cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.ID))
                        val name = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                        val data = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.DATE))
                        val hour1 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR1))
                        val hour2 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR2))
                        val hour3 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR3))
                        val hour4 = cursor?.getString(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUR4))
                        val hourExtra = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                        val idPoint = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra, idPoint))
                    }
                }
                cursor?.close()
                return list

            } catch (e: Exception) {
                return list
            }
        }
    }

    override fun fullPointsById(id: Int, date: String): ArrayList<PointsEntity?> {

        val list: ArrayList<PointsEntity?> = arrayListOf()

        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val idEmployee = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.ID))
                    val name = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4))
                    val hourExtra = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list.add(PointsEntity(idEmployee, name, data, hour1, hour2, hour3, hour4, hourExtra, idPoint))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun fullPointsByName(id: Int): ArrayList<HoursEntity> {

        val list: ArrayList<HoursEntity> = arrayListOf()
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString())

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hour1 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 =
                        cursor?.getString(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUR4))

                    list.add(HoursEntity(hour1, hour2, hour3, hour4))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun removePoints(id: Int): Boolean {

        return try {
            val db = dataBasePoint.writableDatabase
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ?"
            val args = arrayOf(id.toString())

            db.delete(ConstantsPoint.POINT.TABLE_NAME, selection, args)
            true

        } catch (e: Exception) {
            false
        }
    }

    override fun fullPointsByDate(date: String): ArrayList<PointsEntity?> {

        val list: ArrayList<PointsEntity?> = arrayListOf()
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, null, null,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.ID))
                    val name = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4))
                    val hourExtra = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra, idPoint))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun fullPointsByIdAndDate(idEmployee: Int, date: String): PointsFullEntity?{

        var list: PointsFullEntity? = null

        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUR1INT,
                ConstantsPoint.POINT.COLUMNS.HOUR2INT,
                ConstantsPoint.POINT.COLUMNS.HOUR3INT,
                ConstantsPoint.POINT.COLUMNS.HOUR4INT,
                ConstantsPoint.POINT.COLUMNS.PUNCTUATION,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.ID))
                    val nome = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.EMPLOYEE))
                    val data = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.DATE))
                    val hour1 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getString(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4))
                    val hour1Int = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1INT))
                    val hour2Int = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2INT))
                    val hour3Int = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3INT))
                    val hour4Int = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4INT))
                    val punctuation = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.PUNCTUATION))
                    val hourExtra = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUREXTRA))

                    list = PointsFullEntity(id, nome, data, hour1, hour2, hour3, hour4,
                        hour1Int, hour2Int, hour3Int, hour4Int, punctuation, hourExtra)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }

    }

    override fun modifyStatusEmployee(id: Int, status: String): Boolean {

        return try {
            val db = dataBasePoint.writableDatabase
            val projection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ? "
            val args = arrayOf(id.toString())
            val insertValues = ContentValues()

            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS, status)
            db.update(ConstantsEmployee.EMPLOYEE.TABLE_NAME, insertValues, projection, args)

            true

        } catch (e: Exception) { false }
    }

}