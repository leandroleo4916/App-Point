package com.example.app_point.repository

import android.content.ContentValues
import android.database.Cursor
import com.example.app_point.utils.CalculateHours
import com.example.app_point.constants.ConstantsPoint
import com.example.app_point.database.DataBaseEmployee
import com.example.app_point.entity.HoursEntity
import com.example.app_point.entity.HourEntityInt
import com.example.app_point.entity.PointsEntity
import com.example.app_point.entity.PointsHours
import com.example.app_point.interfaces.RepositoryData

class RepositoryPoint(private val dataBasePoint: DataBaseEmployee): RepositoryData {

    private val calculateHourExtras = CalculateHours()
    private val repositoryEmployee = RepositoryEmployee(dataBasePoint)

    override fun setPoint(employee: String, date: String, hour: String, hourInt: Int): Boolean {

        val searchPoint = selectFullPoints(employee, date)

        return try {
            val db = dataBasePoint.writableDatabase
            val projection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(employee, date)
            val insertValues = ContentValues()

            when {
                searchPoint?.employee == null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.DATE, date)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1, hour)
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUR1INT, hourInt)
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

                    val pointInt = selectFullPointsInt(employee, date)
                    val time = repositoryEmployee.consultTime(employee)
                    val extras = calculateHourExtras.calculateHoursExtra(time!!, HourEntityInt(
                        pointInt!!.hora1, pointInt.hora2, pointInt.hora3,
                        pointInt.hora4, pointInt.extra))

                    insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUREXTRA, extras)
                    db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                }
                searchPoint.hora4 != null -> return false
            }
            true

        } catch (e: Exception) { false }
    }

    override fun setPointExtra(employee: String, date: String, hour: Int): Boolean {

        return try {
            val db = dataBasePoint.writableDatabase
            val projection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(employee, date)
            val insertValues = ContentValues()

            insertValues.put(ConstantsPoint.POINT.COLUMNS.HOUREXTRA, hour)
            db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)

            true

        } catch (e: Exception) { false }
    }

    override fun setPointByDate (employee: String, date: String, positionHour: Int,
                                 hour: String, hourInt: Int): Boolean {

        val searchPoint = selectFullPoints(employee, date)

        return try {
            val db = dataBasePoint.writableDatabase
            val projection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(employee, date)
            val insertValues = ContentValues()

            when (searchPoint?.employee) {
                null -> {
                    insertValues.put(ConstantsPoint.POINT.COLUMNS.EMPLOYEE, employee)
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

                    if (hour == "--:--"){
                        insertValues.put(valueInt, hourInt)
                        insertValues.put(valueString, hour)
                        db.insert(ConstantsPoint.POINT.TABLE_NAME, null, insertValues)
                    }else{
                        insertValues.put(valueInt, hourInt)
                        insertValues.put(valueString, hour)
                        db.update(ConstantsPoint.POINT.TABLE_NAME, insertValues, projection, args)
                    }
                }
            }
            true

        } catch (e: Exception) {
            false
        }
    }

    override fun selectFullPoints(nome: String, date: String): PointsEntity? {

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
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome, date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.ID))
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

                    list = PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun selectFullPointsInt(nome: String, date: String): HourEntityInt? {

        var list: HourEntityInt? = null
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.HOUR1INT,
                ConstantsPoint.POINT.COLUMNS.HOUR2INT,
                ConstantsPoint.POINT.COLUMNS.HOUR3INT,
                ConstantsPoint.POINT.COLUMNS.HOUR4INT,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome, date)

            cursor = db.query(
                ConstantsPoint.POINT.TABLE_NAME, projection, selection, args,
                null, null, null
            )

            if (cursor != null && cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val hour1 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR1))
                    val hour2 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR2))
                    val hour3 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR3))
                    val hour4 = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUR4))
                    val extra = cursor?.getInt(cursor.getColumnIndex(
                        ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    list = HourEntityInt(hour1, hour2, hour3, hour4, extra)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun selectPoint(nome: String, date: String): PointsHours? {

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
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome, date)

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

    fun selectPointInt(nome: String, date: String): HourEntityInt? {

        var list: HourEntityInt? = null
        try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1INT,
                ConstantsPoint.POINT.COLUMNS.HOUR2INT,
                ConstantsPoint.POINT.COLUMNS.HOUR3INT,
                ConstantsPoint.POINT.COLUMNS.HOUR4INT,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome, date)

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
                    val extra =
                        cursor?.getInt(cursor.getColumnIndex(ConstantsPoint.POINT.COLUMNS.HOUREXTRA))
                    list = HourEntityInt(hour1, hour2, hour3, hour4, extra)
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun selectHourExtra(nome: String, date: String): String? {

        return try {
            val cursor: Cursor
            val db = dataBasePoint.readableDatabase
            val projection = arrayOf(ConstantsPoint.POINT.COLUMNS.HOUREXTRA)
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome, date)

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
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
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

                    list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun fullPointsToName(nome: String, date: String): ArrayList<PointsEntity?> {

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
                    ConstantsPoint.POINT.COLUMNS.HOUREXTRA
                )
                val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? "
                val args = arrayOf(nome)

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

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra))
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
                    ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                    ConstantsPoint.POINT.COLUMNS.DATE,
                    ConstantsPoint.POINT.COLUMNS.HOUR1,
                    ConstantsPoint.POINT.COLUMNS.HOUR2,
                    ConstantsPoint.POINT.COLUMNS.HOUR3,
                    ConstantsPoint.POINT.COLUMNS.HOUR4,
                    ConstantsPoint.POINT.COLUMNS.HOUREXTRA
                )
                val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                        ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
                val args = arrayOf(nome, date)

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

                        list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra))
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
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
            )
            val selection = ConstantsPoint.POINT.COLUMNS.ID + " = ? AND " +
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

                    list.add(PointsEntity(idEmployee, name, data, hour1, hour2, hour3, hour4, hourExtra))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    override fun fullPointsByName(nome: String): ArrayList<HoursEntity> {

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
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ? AND " +
                    ConstantsPoint.POINT.COLUMNS.DATE + " = ?"
            val args = arrayOf(nome)

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

    override fun removePoints(name: String): Boolean {

        return try {
            val db = dataBasePoint.writableDatabase
            val selection = ConstantsPoint.POINT.COLUMNS.EMPLOYEE + " = ?"
            val args = arrayOf(name)

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
                ConstantsPoint.POINT.COLUMNS.EMPLOYEE,
                ConstantsPoint.POINT.COLUMNS.DATE,
                ConstantsPoint.POINT.COLUMNS.HOUR1,
                ConstantsPoint.POINT.COLUMNS.HOUR2,
                ConstantsPoint.POINT.COLUMNS.HOUR3,
                ConstantsPoint.POINT.COLUMNS.HOUR4,
                ConstantsPoint.POINT.COLUMNS.HOUREXTRA
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

                    list.add(PointsEntity(id, name, data, hour1, hour2, hour3, hour4, hourExtra))
                }
            }
            cursor?.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }
}