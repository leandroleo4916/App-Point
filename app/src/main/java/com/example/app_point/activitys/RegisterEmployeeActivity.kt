package com.example.app_point.activitys

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.databinding.ActivityRegisterEmployeeBinding
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.entity.EmployeeEntity
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class RegisterEmployeeActivity : AppCompatActivity() {

    private val mBusinessEmployee: BusinessEmployee by inject()
    private val mToByteArray: ConverterPhoto by inject()
    private val binding by lazy { ActivityRegisterEmployeeBinding.inflate(layoutInflater) }

    private val PERMISSION_CODE = 1000
    private val IMAGE_GALERY = 1
    private val IMAGE_CAPTURE_CODE = 1001
    private var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        infoEmployee()
        initDate()
        listener()
    }

    private fun listener() {
        binding.imageBack.setOnClickListener { finish() }
        binding.photoEmployee.setOnClickListener { openPopUp() }
        binding.buttomRegisterEmployee.setOnClickListener { extrasId() }
        binding.horario1.setOnClickListener { timePicker(1) }
        binding.horario2.setOnClickListener { timePicker(2) }
        binding.horario3.setOnClickListener { timePicker(3) }
        binding.horario4.setOnClickListener { timePicker(4) }
        binding.textAdmissao.setOnClickListener { calendar(1) }
        binding.textAniversario.setOnClickListener { calendar(2) }
    }

    // Captures date and show in the EditText date and hour
    @SuppressLint("WeekBasedYear")
    private fun initDate(){
        val date = getInstance().time
        val dateTime = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH)
        val dataCurrent = dateTime.format(date)
        binding.textAdmissao.text = dataCurrent
        binding.textAniversario.text = dataCurrent
    }

    // Direction date selected to EditText
    @SuppressLint("SimpleDateFormat", "WeekBasedYear")
    private fun calendar(id: Int) {
        val date = getInstance()
        val dateTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            date.set(DAY_OF_MONTH, dayOfMonth)
            date.set(MONTH, month)
            date.set(YEAR, year)
            when (id) {
                1 -> binding.textAdmissao.text = SimpleDateFormat("dd/MM/YYYY").format(date.time)
                2 -> binding.textAniversario.text = SimpleDateFormat("dd/MM/YYYY").format(date.time)
            }
        }
        DatePickerDialog(
            this, dateTime, date.get(YEAR), date.get(MONTH), date.get(DAY_OF_MONTH)
        ).show()
    }

    //Direction hours selected to EditText
    @SuppressLint("SimpleDateFormat")
    private fun timePicker(id: Int) {

        val cal = getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(HOUR_OF_DAY, hour)
            cal.set(MINUTE, minute)
            when (id) {
                1 -> { binding.horario1.text = SimpleDateFormat("HH:mm").format(cal.time) }
                2 -> { binding.horario2.text = SimpleDateFormat("HH:mm").format(cal.time) }
                3 -> { binding.horario3.text = SimpleDateFormat("HH:mm").format(cal.time) }
                4 -> { binding.horario4.text = SimpleDateFormat("HH:mm").format(cal.time) }
            }
        }
        TimePickerDialog(
            this, timeSetListener, cal.get(HOUR_OF_DAY),
            cal.get(MINUTE), true
        ).show()

    }

    private fun extrasId() {
        val extras = intent.extras
        if (extras != null) {
            saveEmployee(extras.getInt(ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
        } else {
            saveEmployee(id = 0)
        }
    }

    // Receive data to edition
    private fun infoEmployee() {

        val extras = intent.extras
        if (extras != null) {

            val id = extras.getInt(ConstantsEmployee.EMPLOYEE.COLUMNS.ID)
            val infoEmployee: EmployeeEntity = mBusinessEmployee.consultEmployeeWithId(id)!!
            val photo = mToByteArray.converterToBitmap(infoEmployee.photo)
            binding.photoEmployee.setImageBitmap(photo)
            binding.horario1.text = infoEmployee.horario1
            binding.horario2.text = infoEmployee.horario2
            binding.horario3.text = infoEmployee.horario3
            binding.horario4.text = infoEmployee.horario4
            binding.edittextUsername.setText(infoEmployee.nameEmployee)
            binding.edittextEmail.setText(infoEmployee.emailEmployee)
            binding.edittextCargo.setText(infoEmployee.cargoEmployee)
            binding.edittextPhone.setText(infoEmployee.phoneEmployee)
            binding.textAdmissao.text = infoEmployee.admissaoEmployee
            binding.textAniversario.text = infoEmployee.aniversarioEmployee
            binding.textViewHome.text = getString(R.string.editar_funcionario)
            binding.buttomRegisterEmployee.text = getString(R.string.editar)
        }
    }

    // Popup open camera or gallery
    private fun openPopUp() {
        val popMenu = PopupMenu(this, binding.photoEmployee)
        popMenu.menuInflater.inflate(R.menu.popup, popMenu.menu)
        popMenu.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.abrir_camera -> permissionCamera()
                R.id.abrir_galeria -> openGalery()
            }
            true
        }
        popMenu.show()
    }

    // Management the permission of the camera and gallery
    private fun permissionCamera(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            val permission = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permission, PERMISSION_CODE)

        } else {
            openCamera()
        }
        return true
    }

    // Result permission
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permissão negada!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Open camera
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Nova Foto")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Foto Camera")

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    // Capture image of the gallery or camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_GALERY) {
                val selectedImage: Uri? = data!!.data
                binding.photoEmployee.setImageURI(selectedImage)
            }

            else if (resultCode == Activity.RESULT_OK) {
                val extras = data!!.extras!!["data"] as Bitmap
                binding.photoEmployee.setImageBitmap(extras)
            }
        }
    }

    // Open gallery
    private fun openGalery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_GALERY)
    }

    private fun saveEmployee(id: Int) {
        val image = binding.photoEmployee
        val photo = mToByteArray.converterToByteArray(image)
        val hora1 = binding.horario1.text.toString()
        val hora2 = binding.horario2.text.toString()
        val hora3 = binding.horario3.text.toString()
        val hora4 = binding.horario4.text.toString()
        val name = binding.edittextUsername.text.toString()
        val email = binding.edittextEmail.text.toString()
        val cargo = binding.edittextCargo.text.toString()
        val phone = binding.edittextPhone.text.toString()
        val admissao = binding.textAdmissao.text.toString()
        val aniversario = binding.textAniversario.text.toString()

        val edit_horario1 = binding.horario1
        val edit_horario2 = binding.horario2
        val edit_horario3 = binding.horario3
        val edit_horario4 = binding.horario4
        val edit_name = binding.edittextUsername
        val edit_email = binding.edittextEmail
        val edit_cargo = binding.edittextCargo
        val edit_phone = binding.edittextPhone
        val edit_admissao = binding.textAdmissao
        val edit_aniversario = binding.textAniversario

        when {
            false -> Toast.makeText(this, "Tire uma foto!", Toast.LENGTH_SHORT).show()
            hora1 == "" -> edit_horario1.error = "Horário Obrigatório"
            hora2 == "" -> edit_horario2.error = "Horário Obrigatório"
            hora3 == "" -> edit_horario3.error = "Horário Obrigatório"
            hora4 == "" -> edit_horario4.error = "Horário Obrigatório"
            name == "" -> edit_name.error = "Digite Nome"
            email == "" -> edit_email.error = "Digite Email"
            cargo == "" -> edit_cargo.error = "Digite Cargo"
            phone == "" -> edit_phone.error = "Digite Telefone"
            admissao == "" -> edit_admissao.error = "Digite Admissão"
            aniversario == "" -> edit_aniversario.error = "Digite Aniversário"

            else -> setEmployee(EmployeeEntity(id, photo, hora1, hora2, hora3, hora4, name, cargo,
                email, phone, admissao, aniversario))
        }
    }

    private fun setEmployee(employee: EmployeeEntity){
        when(mBusinessEmployee.registerEmployee(employee)){
            "salvo" -> {
                toast(R.string.adicionado_sucesso)
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            "não salvo" -> {
                toast(R.string.nao_foi_possivel_cadastrar)
            }
            "editado" -> {
                toast(R.string.editado_sucesso)
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            "não editado" -> {
                toast(R.string.nao_foi_possivel_editar)
            }
            else -> Toast.makeText(this, R.string.nao_foi_possivel_cadastrar,
                Toast.LENGTH_LONG).show()
        }
    }

    private fun toast(message: Int){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}