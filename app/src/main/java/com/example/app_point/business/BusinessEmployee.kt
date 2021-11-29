package com.example.app_point.business

import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.entity.EmployeeEntity

class BusinessEmployee(private val mRepositoryEmployee: RepositoryEmployee) {

    fun registerEmployee(employeeEntity: EmployeeEntity): String{
        return mRepositoryEmployee.conditionEmployee(employeeEntity)
    }

    fun consultEmployee(): List<String>{
        return mRepositoryEmployee.employeeList()
    }

    fun consultEmployeeByName(nome: String): EmployeeEntity{
        return mRepositoryEmployee.consultEmployee(nome)
    }

    fun consultEmployeeWithId(id: Int): EmployeeEntity?{
        return mRepositoryEmployee.consultDadosEmployeeId(id)
    }

    fun consultPhoto(nome: String): ByteArray?{
        return mRepositoryEmployee.consultPhoto(nome)
    }

}