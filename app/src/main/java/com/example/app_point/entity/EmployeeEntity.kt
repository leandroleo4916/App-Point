package com.example.app_point.entity

import java.io.Serializable

data class EmployeeEntity(
    val id: Int,
    val photo: ByteArray,
    val horario1: String,
    val horario2: String,
    val horario3: String,
    val horario4: String,
    val workload: String,
    val nameEmployee: String,
    val cargoEmployee: String,
    val emailEmployee: String,
    val phoneEmployee: String,
    val admissaoEmployee: String,
    val aniversarioEmployee: String
): Serializable
