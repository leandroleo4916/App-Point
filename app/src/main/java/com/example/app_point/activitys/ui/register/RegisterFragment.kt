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
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.utils.CaptureDateCurrent
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private val mBusinessEmployee: BusinessEmployee by inject()
    private val mToByteArray: ConverterPhoto by inject()
    private val captureDateCurrent: CaptureDateCurrent by inject()
    private val permissionCode = 1000
    private val imageCaptureCode = 1001
    private var imageUri: Uri? = null

    companion object { fun newInstance() = RegisterFragment() }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        val binding = inflater.inflate(R.layout.fragment_register, container, false)

        val args = arguments?.let { it.getSerializable("id") as Int }
        infoEmployee(args, binding)
        initDate(binding)
        listener(binding, args)

        return binding
    }

    private fun infoEmployee(args: Int?, binding: View) {

        if (args != null) {
            val infoEmployee: EmployeeEntity = mBusinessEmployee.consultEmployeeWithId(args)!!
            val photo = mToByteArray.converterToBitmap(infoEmployee.photo)
            binding.run {
                photo_employee.setImageBitmap(photo)
                horario1.text = infoEmployee.horario1
                horario2.text = infoEmployee.horario2
                horario3.text = infoEmployee.horario3
                horario4.text = infoEmployee.horario4
                edittext_username.setText(infoEmployee.nameEmployee)
                edittext_email.setText(infoEmployee.emailEmployee)
                edittext_cargo.setText(infoEmployee.cargoEmployee)
                edittext_phone.setText(infoEmployee.phoneEmployee)
                text_admissao.text = infoEmployee.admissaoEmployee
                text_aniversario.text = infoEmployee.aniversarioEmployee
                textViewHome.text = getString(R.string.editar_funcionario)
                buttom_register_employee.text = getString(R.string.editar)
            }
        }
    }

    private fun listener(binding: View, args: Int?) {
        binding.run {
            photo_employee.setOnClickListener { openPopUp(binding) }
            buttom_register_employee.setOnClickListener { extrasId (args, binding) }
            horario1.setOnClickListener { timePicker(1, binding) }
            horario2.setOnClickListener { timePicker(2, binding) }
            horario3.setOnClickListener { timePicker(3, binding) }
            horario4.setOnClickListener { timePicker(4, binding) }
            text_admissao.setOnClickListener { calendar(1, binding) }
            text_aniversario.setOnClickListener { calendar(2, binding) }
        }
    }

    private fun initDate(binding: View) {
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
        val simple = SimpleDateFormat("HH:mm", local).format(cal.time)
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            when (id) {
                1 -> { binding.horario1.text = simple }
                2 -> { binding.horario2.text = simple }
                3 -> { binding.horario3.text = simple }
                4 -> { binding.horario4.text = simple }
            }
        }
        TimePickerDialog(
            context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), true
        ).show()

    }

    private fun extrasId (id: Int?, binding: View) {
        if (id != null) saveEmployee(id, binding)
        else saveEmployee(0, binding)
    }

    private fun openPopUp(binding: View) {
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

    // Management the permission of the camera and gallery
    private fun permissionCamera(): Boolean {
        if (context?.let {
                ContextCompat.checkSelfPermission (
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
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    toast(R.string.permissao_negada)
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
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, imageCaptureCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imageCaptureCode && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photo_employee.setImageBitmap(imageBitmap)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startForResult.launch(intent)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                photo_employee.setImageURI(result.data?.data)
            }
        }

    private fun saveEmployee(id: Int, binding: View) {

        val image = binding.photo_employee
        val photo = mToByteArray.converterToByteArray(image)
        val hora1 = binding.horario1.text.toString()
        val hora2 = binding.horario2.text.toString()
        val hora3 = binding.horario3.text.toString()
        val hora4 = binding.horario4.text.toString()
        val name = binding.edittext_username.text.toString()
        val email = binding.edittext_email.text.toString()
        val cargo = binding.edittext_cargo.text.toString()
        val phone = binding.edittext_phone.text.toString()
        val admission = binding.text_admissao.text.toString()
        val birth = binding.text_aniversario.text.toString()

        when {
            hora1.isEmpty() -> binding.horario1.error = getString(R.string.horario_obrigatorio)
            hora2.isEmpty() -> binding.horario2.error = getString(R.string.horario_obrigatorio)
            hora3.isEmpty() -> binding.horario3.error = getString(R.string.horario_obrigatorio)
            hora4.isEmpty() -> binding.horario4.error = getString(R.string.horario_obrigatorio)
            name.isEmpty() -> binding.edittext_username.error = getString(R.string.digite_nome)
            email.isEmpty() -> binding.edittext_email.error = getString(R.string.digite_email)
            cargo.isEmpty() -> binding.edittext_cargo.error = getString(R.string.digite_cargo)
            phone.isEmpty() -> binding.edittext_phone.error = getString(R.string.digite_phone)
            admission.isEmpty() -> binding.text_admissao.error = getString(R.string.digite_admissao)
            birth.isEmpty() -> binding.text_aniversario.error = getString(R.string.digite_aniversario)

            else -> setEmployee(EmployeeEntity(id, photo, hora1, hora2, hora3, hora4, name, cargo,
                email, phone, admission, birth))
        }
    }

    private fun setEmployee(employee: EmployeeEntity){
        when(mBusinessEmployee.registerEmployee(employee)){
            "salvo" -> {
                toast(R.string.adicionado_sucesso)
            }
            "não salvo" -> {
                toast(R.string.nao_foi_possivel_cadastrar)
            }
            "editado" -> {
                toast(R.string.editado_sucesso)
            }
            "não editado" -> {
                toast(R.string.nao_foi_possivel_editar)
            }
            else -> toast(R.string.nao_foi_possivel_cadastrar)
        }
    }

    private fun toast(message: Int){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}