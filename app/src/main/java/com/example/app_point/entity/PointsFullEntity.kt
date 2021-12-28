package com.example.app_point.entity

import java.io.Serializable

data class PointsFullEntity(
    val id: Int?,
    var employee: String?,
    var data: String?,
    var hora1: String?,
    var hora2: String?,
    var hora3: String?,
    var hora4: String?,
    var hora1Int: Int?,
    var hora2Int: Int?,
    var hora3Int: Int?,
    var hora4Int: Int?,
    var punctuation: Int?,
    var horaExtra: Int?
): Serializable
