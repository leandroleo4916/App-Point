package com.example.app_point.repository

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.constants.ConstantsExtras
import com.example.app_point.constants.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.entity.*
import com.example.app_point.interfaces.RepositoryData
import com.example.app_point.utils.CalculateHours

class RepositoryPoint(dataBasePoint: DataBaseEmployee): RepositoryData {

    private val calculateHourExtras = CalculateHours()
    private val repositoryEmployee = RepositoryEmployee(dataBasePoint)
    private val dbWrite: SQLiteDatabase = dataBasePoint.writableDatabase
    private val dbRead: SQLiteDatabase = dataBasePoint.readableDatabase

    override fun setPoint(id: Int, employee: String, date: String, hour: String, hourInt: Int): Boolean {

        val searchPoint = selectFullPoints(id, date)

        return try {
            val insertValues = ContentValues()

            when {
                searchPoint?.idEmployee == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1, hour)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1INT, hourInt)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE, id)
                    dbWrite.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
                }

                searchPoint.data != null && searchPoint.hora1 == null -> {
                    savePointByPosition(insertValues, ConstantsPoint.POINT.COLUMNS.HOUR1, hour,
                        ConstantsPoint.POINT.COLUMNS.HOUR1INT, hourInt, id, date)
                }

                searchPoint.data != null && searchPoint.hora1 != null && searchPoint.hora2 == null -> {
                    savePointByPosition(insertValues, ConstantsPoint.POINT.COLUMNS.HOUR2, hour,
                        ConstantsPoint.POINT.COLUMNS.HOUR2INT, hourInt, id, date)
                }

                searchPoint.data != null && searchPoint.hora1 != null && searchPoint.hora2 != null
                        && searchPoint.hora3 == null -> {
                    savePointByPosition(insertValues, ConstantsPoint.POINT.COLUMNS.HOUR3, hour,
                        ConstantsPoint.POINT.COLUMNS.HOUR3INT, hourInt, id, date)
                }

                searchPoint.data != null && searchPoint.hora1 != null && searchPoint.hora2 != null
                        && searchPoint.hora3 != null && searchPoint.hora4 == null -> {
                    savePointByPosition(insertValues, ConstantsPoint.POINT.COLUMNS.HOUR4, hour,
                        ConstantsPoint.POINT.COLUMNS.HOUR4INT, hourInt, id, date)

                    saveHoursPunctuation(id, date)
                }
                searchPoint.hora4 != null -> return false
            }
            true

        } catch (e: Exception) { false }
    }

    private fun savePointByPosition(insertValues: ContentValues, hourString: String,
                                    hourStr: String, hourInt: String,
                                    hour: Int, id: Int, date: String) {

        val projection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
        val args = arrayOf(id.toString(), date)

        insertValues.put(hourString, hourStr)
        insertValues.put(hourInt, hour)
        dbWrite.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
    }

    private fun saveHoursPunctuation(id: Int, date: String){

        val projection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
        val args = arrayOf(id.toString(), date)
        val insertValues = ContentValues()

        val pointInt = selectFullPointsInt(id, date)
        val timeEmployee = repositoryEmployee.consultHorarioInAndOut(id)
        val punctuation = calculateHourExtras.punctuation(timeEmployee!!, pointInt)

        insertValues.put(ConstantsPoint.POINT.COLUMNS.PUNCTUATION, punctuation)
        dbWrite.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

        saveTotalBankHoursExtraAndDone(id, pointInt!!)
    }

    private fun saveTotalBankHoursExtraAndDone(id: Int, pointInt: HourEntityInt){

        val workload = repositoryEmployee.consultCargaHoraria(id)
        val hours = calculateHourExtras.calculateHoursExtra(workload, HourEntityInt(
            pointInt.hora1, pointInt.hora2, pointInt.hora3, pointInt.hora4, 0, 0))

        val consult = consultTotalExtraByIdEmployee(id)
        val insertValues = ContentValues()

        if (consult == null){
            try {

                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.ID, id)
                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.EXTRA, hours!!.extra)
                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.FEITAS, hours.feita)

                dbWrite.insert(ConstantsExtras.EXTRA.TABLE_NAME, null, insertValues)
            }
            catch (e: Exception){ }
        }
        else {

            val totalExtra = if (hours!!.extra < 0){ consult.extra - hours.extra
            } else{ consult.extra + hours.extra }

            val totalFeita = consult.feita + hours.feita

            try {
                val projection = ConstantsExtras.EXTRA.COLUMNS.ID + " = ?"
                val args = arrayOf(id.toString())

                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.EXTRA, totalExtra)
                insertValues.put(ConstantsExtras.EXTRA.COLUMNS.FEITAS, totalFeita)

                dbWrite.update(ConstantsExtras.EXTRA.TABLE_NAME, insertValues, projection, args)
            }
            catch (e: Exception){ }
        }
    }

    fun consultTotalExtraByIdEmployee(id: Int): ExtraDoneEntity? {

        var value: ExtraDoneEntity? = null
        try {
            val cursor: Cursor
            val projection = arrayOf(
                ConstantsExtras.EXTRA.COLUMNS.EXTRA,
                ConstantsExtras.EXTRA.COLUMNS.FEITAS)
            val selection = ConstantsExtras.EXTRA.COLUMNS.ID + " = ? "
            val args = arrayOf(id.toString())

            cursor = dbRead.query(
                ConstantsExtras.EXTRA.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val extra = cursor?.getInt(
                        cursor.getColumnIndex(ConstantsExtras.EXTRA.COLUMNS.EXTRA))
                    val feita = cursor?.getInt(
                        cursor.getColumnIndex(ConstantsExtras.EXTRA.COLUMNS.FEITAS))

                    value = ExtraDoneEntity(extra!!, feita!!)
                }
            }
            cursor?.close()
            return value

        } catch (e: Exception) {
            return value
        }
    }

    override fun setPointExtra(idEmployee: Int, extra: Int, feitas: Int): Boolean {

        return try {
            val projection = ConstantsExtras.EXTRA.COLUMNS.ID + " = ? "
            val args = arrayOf(idEmployee.toString())
            val insertValues = ContentValues()

            insertValues.put(ConstantsExtras.EXTRA.COLUMNS.EXTRA, extra)
            insertValues.put(ConstantsExtras.EXTRA.COLUMNS.FEITAS, feitas)
            dbWrite.update(ConstantsExtras.EXTRA.TABLE_NAME, insertValues, projection, args)

            true

        } catch (e: Exception) { false }
    }

    override fun setPointByDate (idEmployee: Int, date: String, positionHour: Int,
                                 hour: String, hourInt: Int): Boolean {

        val searchPoint = selectFullPoints(idEmployee, date)

        return try {
            val projection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)
            val insertValues = ContentValues()

            when (searchPoint?.idEmployee) {
                null -> {

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

                    insertValues.put(ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE, idEmployee)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
                    insertValues.put(valueInt, hourInt)
                    insertValues.put(valueString, hour)

                    dbWrite.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)

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
                    dbWrite.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

                    if (searchPoint.hora1 != null && searchPoint.hora2 != null &&
                        searchPoint.hora3 != null && searchPoint.hora4 != null ){

                            saveHoursPunctuation(idEmployee, date)
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
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)

            cursor = dbRead.query(
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
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list = PointsEntity(id, name, data, hour1, hour2, hour3, hour4, 0, idPoint)
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
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1INT,
                ConstantsPoint.POINT.COLUMNS.HOUR2INT,
                ConstantsPoint.POINT.COLUMNS.HOUR3INT,
                ConstantsPoint.POINT.COLUMNS.HOUR4INT,
                ConstantsPoint.POINT.COLUMNS.PUNCTUATION
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)

            cursor = dbRead.query(
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
                    list = HourEntityInt(hour1, hour2, hour3, hour4, punctuation, 0)
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
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)

            cursor = dbRead.query(
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
                    list = PointsHours(data, hour1, hour2, hour3, hour4, 0)
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
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1INT,
                ConstantsPoint.POINT.COLUMNS.HOUR2INT,
                ConstantsPoint.POINT.COLUMNS.HOUR3INT,
                ConstantsPoint.POINT.COLUMNS.HOUR4INT
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)

            cursor = dbRead.query(
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
                    list = HourEntityInt(hour1, hour2, hour3, hour4, 0, 0)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun selectHourExtra(id: Int, date: String): String? {
        return null
    }

    override fun fullPoints(): ArrayList<PointsEntity?> {

        val list: ArrayList<PointsEntity?> = arrayListOf()
        try {
            val cursor: Cursor
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )

            cursor = dbRead.query(
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
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, 0, idPoint))
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
                val projection = arrayOf(
                    ConstantsPoint.POINT.COLUMNS.ID,
                    ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                    ConstantsPoint.POINT.COLUMNS.DATE,
                    ConstantsPoint.POINT.COLUMNS.HOUR1,
                    ConstantsPoint.POINT.COLUMNS.HOUR2,
                    ConstantsPoint.POINT.COLUMNS.HOUR3,
                    ConstantsPoint.POINT.COLUMNS.HOUR4,
                    ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
                )
                val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? "
                val args = arrayOf(idEmployee.toString())

                cursor = dbRead.query(
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
                        val idPoint = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, 0, idPoint))
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
                val projection = arrayOf(
                    ConstantsPoint.POINT.COLUMNS.ID,
                    ConstantsPoint.POINT.COLUMNS.DATE,
                    ConstantsPoint.POINT.COLUMNS.HOUR1,
                    ConstantsPoint.POINT.COLUMNS.HOUR2,
                    ConstantsPoint.POINT.COLUMNS.HOUR3,
                    ConstantsPoint.POINT.COLUMNS.HOUR4,
                    ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
                )
                val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                        ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
                val args = arrayOf(idEmployee.toString(), date)

                cursor = dbRead.query(
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
                        val idPoint = cursor?.getInt(cursor.getColumnIndex(
                            ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, 0, idPoint))
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
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString(), date)

            cursor = dbRead.query(
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
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list.add(PointsEntity(idEmployee, name, data, hour1, hour2, hour3, hour4, 0, idPoint))
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
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(id.toString())

            cursor = dbRead.query(
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
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ?"
            val args = arrayOf(id.toString())

            dbWrite.delete(ConstantsPoint.POINT.TABLE_NAME, selection, args)
            true

        } catch (e: Exception) {
            false
        }
    }

    override fun fullPointsByDate(date: String): ArrayList<PointsEntity?> {

        val list: ArrayList<PointsEntity?> = arrayListOf()
        try {
            val cursor: Cursor
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.ID,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE
            )

            cursor = dbRead.query(
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
                    val idPoint = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE))

                    list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, 0, idPoint))
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
                ConstantsPoint.POINT.COLUMNS.PUNCTUATION
            )
            val selection = ConstantsPoint.POINT.COLUMNS.IDEMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(idEmployee.toString(), date)

            cursor = dbRead.query(
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

                    list = PointsFullEntity(id, nome, data, hour1, hour2, hour3, hour4,
                        hour1Int, hour2Int, hour3Int, hour4Int, punctuation, 0)
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
            val projection = ConstantsEmployee.EMPLOYEE.COLUMNS.ID + " = ? "
            val args = arrayOf(id.toString())
            val insertValues = ContentValues()

            insertValues.put(ConstantsEmployee.EMPLOYEE.COLUMNS.STATUS, status)
            dbWrite.update(ConstantsEmployee.EMPLOYEE.TABLE_NAME, insertValues, projection, args)

            true

        } catch (e: Exception) { false }
    }

}