package com.example.app_point.interfaces

import com.example.app_point.entity.HoursEntity
import com.example.app_point.entity.HourEntityInt
import com.example.app_point.entity.PointsEntity
import com.example.app_point.entity.PointsHours

interface RepositoryData {
    fun setPoint(employee: String, date: String, hour: String, hourInt: Int): Boolean
    fun setPointExtra(employee: String, date: String, hour: Int): Boolean
    fun selectFullPoints(nome: String, date: String): PointsEntity?
    fun selectFullPointsInt(nome: String, date: String): HourEntityInt?
    fun selectHourExtra(nome: String, date: String): String?
    fun fullPoints(): ArrayList<PointsEntity?>
    fun fullPointsToName(nome: String, date: String): ArrayList<PointsEntity?>
    fun fullPointsById(id: Int, date: String): ArrayList<PointsEntity?>
    fun fullPointsByName(nome: String): ArrayList<HoursEntity>
    fun removePoints(name: String): Boolean
    fun selectPoint(nome: String, date: String): PointsHours?
    fun setPointByDate(employee: String, date: String, positionHour: Int,
                       hour: String, hourInt: Int): Boolean
    fun fullPointsByDate(date: String): ArrayList<PointsEntity?>
}