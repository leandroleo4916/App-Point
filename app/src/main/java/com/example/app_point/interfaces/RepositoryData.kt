package com.example.app_point.interfaces

import com.example.app_point.entity.*

interface RepositoryData {
    fun setPoint(id: Int, employee: String, date: String, hour: String, hourInt: Int): Boolean
    fun setPointExtra(idEmployee: Int, extra: Int, feitas: Int): Boolean
    fun selectFullPoints(idEmployee: Int, date: String): PointsEntity?
    fun selectFullPointsInt(idEmployee: Int, date: String): HourEntityInt?
    fun selectHourExtra(id: Int, date: String): String?
    fun fullPoints(): ArrayList<PointsEntity?>
    fun fullPointsToName(idEmployee: Int, date: String): ArrayList<PointsEntity?>
    fun fullPointsById(id: Int, date: String): ArrayList<PointsEntity?>
    fun fullPointsByName(id: Int): ArrayList<HoursEntity>
    fun removePoints(id: Int): Boolean
    fun selectPoint(id: Int, date: String): PointsHours?
    fun setPointByDate(idEmployee: Int, date: String, positionHour: Int, hour: String, hourInt: Int): Boolean
    fun fullPointsByDate(date: String): ArrayList<PointsEntity?>
    fun fullPointsByIdAndDate(idEmployee: Int, date: String): PointsFullEntity?
    fun modifyStatusEmployee(id: Int, status: String): Boolean
}