package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryEmployee
import com.example.app_point.utils.EmployeeEntity

class BusinessEmployee(context: Context) {

    private val mRespositoryEmployee: RepositoryEmployee = RepositoryEmployee(context)

    fun registerEmployee(photo: ByteArray, hour1: String, hour2: String, hour3: String, hour4: String, name: String,
                        cargo: String, email: String, phone: String, admissao: String,
                        aniversario: String): Boolean{

        return mRespositoryEmployee.getEmployee(
            photo, hour1, hour2, hour3, hour4, name, cargo, email, phone, admissao, aniversario)

    }

    fun consultEmployee(): List<String>{
        return mRespositoryEmployee.employeeList()
    }

    fun consultDadosEmployee(nome: String): EmployeeEntity?{
        return mRespositoryEmployee.consultDadosEmployee(nome)
    }

}