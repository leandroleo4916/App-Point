package com.example.app_point.interfaces

import com.example.app_point.entity.HoursEntity
import com.example.app_point.entity.PointsEntity
import com.example.app_point.entity.PointsHours

interface RepositoryData {
    fun setPoint(employee: String, date: String, hour: String): Boolean
    fun selectFullPoints(nome: String, date: String): PointsEntity?
    fun fullPoints(): ArrayList<PointsEntity?>
    fun fullPointsToName(nome: String, date: String): ArrayList<PointsEntity?>
    fun fullPointsByName(nome: String): ArrayList<HoursEntity>
    fun removePoints(name: String): Boolean
    fun selectPoint(nome: String, date: String): PointsHours?
}