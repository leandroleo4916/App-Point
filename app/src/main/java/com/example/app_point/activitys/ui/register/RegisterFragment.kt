package com.example.app_point.activitys.ui.register

import android.Manifest
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.entity.EmployeeEntityFull
import com.example.app_point.entity.HourEntityInt
import com.example.app_point.interfaces.ItemClickOpenRegister
import com.example.app_point.utils.CalculateHours
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.fragment_register.view.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var itemClickOpenRegister: ItemClickOpenRegister
    private lateinit var registerViewModel: RegisterViewModel
    private val mBusinessEmployee: BusinessEmployee by inject()
    private val mToByteArray: ConverterPhoto by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val calculateHours: CalculateHours by inject()
    private val permissionCode = 1000
    private val imageCaptureCode = 1001
    private var imageUri: Uri? = null
    private lateinit var binding: View
    var employee: EmployeeEntityFull? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        binding = inflater.inflate(R.layout.fragment_register, container, false)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val args = arguments?.let { it.getSerializable("id") as Int }
        if (args != null){
            employee = mBusinessEmployee.consultEmployeeWithId(args)!!
        }

        infoEmployee()
        initDate()
        listener(args)

        return binding
    }

    private fun infoEmployee() {

        if (employee != null){
            val photo = mToByteArray.converterToBitmap(employee!!.photo)
            binding.run {
                photo_employee.setImageBitmap(photo)
                horario1.text = calculateHours.convertMinutesInHoursString(employee!!.horario1)
                horario2.text = calculateHours.convertMinutesInHoursString(employee!!.horario2)
                horario3.text = calculateHours.convertMinutesInHoursString(employee!!.horario3)
                horario4.text = calculateHours.convertMinutesInHoursString(employee!!.horario4)
                edittext_username.setText(employee!!.nameEmployee)
                edittext_email.setText(employee!!.emailEmployee)
                edittext_cargo.setText(employee!!.cargoEmployee)
                edittext_phone.setText(employee!!.phoneEmployee)
                text_admissao.text = employee!!.admissaoEmployee
                text_aniversario.text = employee!!.aniversarioEmployee
                textViewHome.text = getString(R.string.editar_funcionario)
                buttom_register_employee.text = getString(R.string.editar)
            }
        }

    }

    private fun listener(args: Int?) {
        binding.run {
            photo_employee.setOnClickListener { openPopUp() }
            buttom_register_employee.setOnClickListener { extrasId(args) }
            horario1.setOnClickListener { timePicker(1, binding) }
            horario2.setOnClickListener { timePicker(2, binding) }
            horario3.setOnClickListener { timePicker(3, binding) }
            horario4.setOnClickListener { timePicker(4, binding) }
            text_admissao.setOnClickListener { calendar(1, binding) }
            text_aniversario.setOnClickListener { calendar(2, binding) }
            image_back.setOnClickListener { activity?.onBackPressed() }
        }
    }

    private fun initDate() {
        val dataCurrent = captureDateCurrent.captureDateCurrent()
        binding.text_admissao.text = dataCurrent
        binding.text_aniversario.text = dataCurrent
    }

    private fun calendar(id: Int, binding: View) {
        val date = Calendar.getInstance()
        val local = Locale("pt", "BR")
        val dateTime = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date.set(Calendar.MONTH, month)
            date.set(Calendar.YEAR, year)
            when (id) {
                1 -> binding.text_admissao.text =
                    SimpleDateFormat("dd/MM/yyyy", local).format(date.time)
                2 -> binding.text_aniversario.text =
                    SimpleDateFormat("dd/MM/yyyy", local).format(date.time)
            }
        }
        context?.let {
            DatePickerDialog(
                it, dateTime,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun timePicker(id: Int, binding: View) {

        val cal = Calendar.getInstance()
        val local = Locale("pt", "BR")
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            when (id) {
                1 -> binding.horario1.text =
                    SimpleDateFormat("HH:mm", local).format(cal.time)
                2 -> binding.horario2.text =
                    SimpleDateFormat("HH:mm", local).format(cal.time)
                3 -> binding.horario3.text =
                    SimpleDateFormat("HH:mm", local).format(cal.time)
                4 -> binding.horario4.text =
                    SimpleDateFormat("HH:mm", local).format(cal.time)
            }
        }
        TimePickerDialog(
            context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), true
        ).show()

    }

    private fun extrasId(id: Int?) {
        if (id != null) saveEmployee(id)
        else saveEmployee(0)
    }

    private fun openPopUp() {
        val popMenu = PopupMenu(context, binding.photo_employee)
        popMenu.menuInflater.inflate(R.menu.popup, popMenu.menu)
        popMenu.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.abrir_camera -> permissionCamera()
                R.id.abrir_galeria -> openGallery()
            }
            true
        }
        popMenu.show()
    }

    private fun permissionCamera(): Boolean {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED ||
            context?.let {
                ContextCompat.checkSelfPermission(
                    it, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_DENIED
        ) {
            val permission = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permission, permissionCode)

        } else { openCamera() }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else { toast(R.string.permissao_negada) }
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Nova Foto")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Foto Camera")

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, imageCaptureCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imageCaptureCode && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.photo_employee.setImageBitmap(imageBitmap)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                binding.photo_employee.setImageURI(result.data?.data)
            }
        }

    private fun saveEmployee(id: Int) {

        val image = binding.photo_employee
        val photo = mToByteArray.converterToByteArray(image)
        val hora1 = calculateHours.converterHoursInMinutes(binding.horario1.text.toString())
        val hora2 = calculateHours.converterHoursInMinutes(binding.horario2.text.toString())
        val hora3 = calculateHours.converterHoursInMinutes(binding.horario3.text.toString())
        val hora4 = calculateHours.converterHoursInMinutes(binding.horario4.text.toString())
        val workload = calculateHours.calculateTimeEmployee(HourEntityInt(hora1,
            hora2, hora3, hora4, 0, 0))
        val name = binding.edittext_username.text.toString()
        val email = binding.edittext_email.text.toString()
        val cargo = binding.edittext_cargo.text.toString()
        val phone = binding.edittext_phone.text.toString()
        val admission = binding.text_admissao.text.toString()
        val birth = binding.text_aniversario.text.toString()

        val status = when (employee){
            null -> "Trabalhando"
            else -> employee!!.status
        }

        when {
            name.isEmpty() -> binding.edittext_username.error = getString(R.string.digite_nome)
            email.isEmpty() -> binding.edittext_email.error = getString(R.string.digite_email)
            cargo.isEmpty() -> binding.edittext_cargo.error = getString(R.string.digite_cargo)
            phone.isEmpty() -> binding.edittext_phone.error = getString(R.string.digite_phone)
            admission.isEmpty() -> binding.text_admissao.error = getString(R.string.digite_admissao)
            birth.isEmpty() -> binding.text_aniversario.error = getString(R.string.digite_aniversario)

            else -> setEmployee(EmployeeEntity(id, status, photo, hora1, hora2, hora3,
                hora4, workload, name, cargo, email, phone, admission, birth))
        }
    }

    private fun setEmployee(employee: EmployeeEntity){
        when (mBusinessEmployee.registerEmployee(employee)){
            "salvo" -> {
                dialogSaveEmployee()
            }
            "não salvo" -> {
                dialogErrorSaveOrEditEmployee("Cadastrar")
            }
            "editado" -> {
                dialogEditEmployee()
            }
            "não editado" -> {
                dialogErrorSaveOrEditEmployee("Editar")
            }
            else -> dialogErrorSaveOrEditEmployee("Cadastrar")
        }
    }

    private fun openFragmentProfile() {
        if (context is ItemClickOpenRegister) {
            itemClickOpenRegister = context as ItemClickOpenRegister }
        itemClickOpenRegister.openFragmentRegister()
    }

    private fun dialogSaveEmployee(){
        SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Registrado com sucesso!")
            .setContentText("Deseja registrar mais?")
            .setCancelText("Não, voltar!")
            .setConfirmText("Adicionar mais!")
            .setConfirmClickListener { sDialog -> sDialog.cancel()
                openFragmentProfile()
            }
            .setCancelClickListener { sDialog -> sDialog.cancel()
                activity?.onBackPressed()
            }
            .show()
    }

    private fun dialogEditEmployee(){
        SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Editado com sucesso!")
            .setCancelText("Voltar!")
            .setConfirmText("Editar novamente!")
            .setConfirmClickListener { sDialog -> sDialog.cancel() }
            .setCancelClickListener { sDialog -> sDialog.cancel()
                activity?.onBackPressed()
            }
            .show()
    }

    private fun dialogErrorSaveOrEditEmployee(value: String){
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Não foi possível $value!")
            .show()
    }

    private fun toast(message: Int){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}