package com.example.app_point.repository

import androidx.lifecycle.liveData
import com.example.app_point.interfaces.EmployeeApi
import java.net.ConnectException

sealed class Resultado<out R> {
    data class Sucesso<out T>(val dado: T?) : Resultado<T?>()
    data class Erro(val exception: Exception) : Resultado<Nothing>()
}

class RepositoryFirebase(private val service: EmployeeApi) {

    fun getEmployee() = liveData {
        try {
            val response = service.getEmployeeApi()
            if(response.isSuccessful){
                emit(Resultado.Sucesso(dado = response.body())) }
            else {
                emit(Resultado.Erro(exception = Exception("Falha ao buscar o funcionário")))
            }
        } catch (e: ConnectException) {
            emit(Resultado.Erro(exception = Exception("Falha na comunicação com API")))
        }
        catch (e: Exception) {
            emit(Resultado.Erro(exception = e))
        }
    }
}