package com.example.app_point.interfaces

import com.example.app_point.entity.EmployeeNameAndPhoto

interface ItemEmployee {
    fun openFragmentProfile(employee: EmployeeNameAndPhoto)
    fun openFragmentRegister(id: Int)
}