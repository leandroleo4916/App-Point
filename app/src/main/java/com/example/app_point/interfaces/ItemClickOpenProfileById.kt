package com.example.app_point.interfaces

import com.example.app_point.entity.EmployeeNameAndPhoto

interface ItemClickOpenProfileById {
    fun openFragmentProfileById(employee: EmployeeNameAndPhoto)
}