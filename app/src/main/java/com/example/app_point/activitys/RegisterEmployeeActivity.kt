package com.example.app_point.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import kotlinx.android.synthetic.main.activity_register_employee.*
import kotlinx.android.synthetic.main.activity_register_employee.edittext_email
import kotlinx.android.synthetic.main.activity_register_employee.edittext_username
import kotlinx.android.synthetic.main.activity_register_employee.image_back

class RegisterEmployeeActivity : AppCompatActivity(), View.OnClickListener {

    private val mBusinessEmployee: BusinessEmployee = BusinessEmployee(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_employee)

        listener()
    }

    private fun listener(){
        image_back.setOnClickListener(this)
        buttom_register_employee.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back -> finish()
            buttom_register_employee -> registerEmployee()
        }
    }

    private fun registerEmployee(){
        val hora1 = horario1.text.toString()
        val hora2 = horario2.text.toString()
        val hora3 = horario3.text.toString()
        val hora4 = horario4.text.toString()
        val name = edittext_username.text.toString()
        val email = edittext_email.text.toString()
        val cargo = edittext_cargo.text.toString()
        val phone = edittext_phone.text.toString()
        val admissao = edittext_admissao.text.toString()
        val aniversario = edittext_aniversário.text.toString()

        val edit_horario1 = horario1
        val edit_horario2 = horario2
        val edit_horario3 = horario3
        val edit_horario4 = horario4
        val edit_name = edittext_username
        val edit_email = edittext_email
        val edit_cargo = edittext_cargo
        val edit_phone = edittext_phone
        val edit_admissao = edittext_admissao
        val edit_aniversario = edittext_aniversário

        when{
            hora1 == "" -> {
                edit_horario1.error = "Horário Obrigatório"
            }
            hora2 == "" -> {
                edit_horario2.error = "Horário Obrigatório"
            }
            hora3 == "" -> {
                edit_horario3.error = "Horário Obrigatório"
            }
            hora4 == ""  -> {
                edit_horario4.error = "Horário Obrigatório"
            }
            name == ""  -> {
                edit_name.error = "Digite Nome"
            }
            email == "" -> {
                edit_email.error = "Digite Email"
            }
            cargo == "" -> {
                edit_cargo.error = "Digite Cargo"
            }
            phone == ""  -> {
                edit_phone.error = "Digite Telefone"
            }
            admissao == "" -> {
                edit_admissao.error = "Digite Admissão"
            }
            aniversario == ""  -> {
                edit_aniversario.error = "Digite Aniversário"
            }
            mBusinessEmployee.registerEmplyee(name, cargo, email, phone, admissao, aniversario,
                hora1, hora2, hora3, hora4) ->
                Toast.makeText(this, R.string.cadastro_feito, Toast.LENGTH_SHORT).show()
            }
        }
}