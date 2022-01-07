package com.example.app_point.business

import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.EmployeeIdAndName

class BusinessEmployee(private val repositoryEmployee: RepositoryEmployee) {

    fun registerEmployee(employeeEntity: EmployeeEntity): String{
        return repositoryEmployee.conditionEmployee(employeeEntity)
    }

    fun consultEmployeeList(): List<String>{
        return repositoryEmployee.employeeList()
    }

    fun consultEmployeeWithId(id: Int?): EmployeeEntity?{
        return id?.let { repositoryEmployee.consultDataEmployeeId(it) }
    }

    fun consultPhoto(id: Int): ByteArray?{
        return repositoryEmployee.consultPhoto(id)
    }

    fun consultNameEmployeeById(id: Int): String? {
        return repositoryEmployee.consultNameEmployeeById(id)
    }

    fun consultIdEmployeeByName(name: String): Int {
        return repositoryEmployee.consultIdEmployeeByName(name)!!
    }

}