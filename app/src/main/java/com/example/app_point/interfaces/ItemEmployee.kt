package com.example.app_point.interfaces

import com.example.app_point.entity.EmployeeEntity

interface ItemEmployee {
    fun openFragmentProfile(employee: EmployeeEntity)
    fun openFragmentRegister(id: Int)
}