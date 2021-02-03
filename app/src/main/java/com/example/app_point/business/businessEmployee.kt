package com.example.app_point.business

import android.content.Context
import com.example.app_point.repository.RepositoryEmployee

class BusinessEmployee(context: Context) {

    private val mRespositoryEmployee: RepositoryEmployee = RepositoryEmployee(context)

    fun registerEmplyee(name: String, cargo: String, email: String, phone: String, admissao: String,
                        aniversario: String, hour1: String, hour2: String, hour3: String,
                        hour4: String): Boolean{

        return mRespositoryEmployee.getEmployee(name,  cargo, email, phone, admissao,
                                                aniversario, hour1, hour2, hour3, hour4)

    }

}