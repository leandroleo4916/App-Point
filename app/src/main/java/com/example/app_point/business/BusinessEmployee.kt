package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.entity.EmployeeEntity

class BusinessEmployee(context: Context) {

    private val mRepositoryEmployee: RepositoryEmployee = RepositoryEmployee(context)

    fun registerEmployee(id: Int, photo: ByteArray, hour1: String, hour2: String, hour3: String, hour4: String, name: String,
                        cargo: String, email: String, phone: String, admissao: String, aniversario: String): Boolean{

        return mRepositoryEmployee.conditionEmployee(id, photo, hour1, hour2, hour3, hour4, name,
            cargo, email, phone, admissao, aniversario)
    }

    fun consultEmployee(): List<String>?{
        return mRepositoryEmployee.employeeList()
    }

    fun consultDadosEmployee(nome: String): EmployeeEntity?{
        return mRepositoryEmployee.consultDataEmployee(nome)
    }

    fun consultEmployeeWithId(id: Int): EmployeeEntity?{
        return mRepositoryEmployee.consultDadosEmployeeId(id)
    }

    fun consultPhoto(nome: String): ByteArray?{
        return mRepositoryEmployee.consultPhoto(nome)
    }

    fun consultHours(nome: String): List<String>? {
        return mRepositoryEmployee.consultHours(nome)
    }
}