package com.example.app_point.activitys

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.edmodo.cropper.CropImageView
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.utils.ConverterPhoto
import kotlinx.android.synthetic.main.activity_register_employee.*
import java.io.ByteArrayOutputStream

class RegisterEmployeeActivity : AppCompatActivity(), View.OnClickListener {

    private val mBusinessEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mToByteArray: ConverterPhoto = ConverterPhoto()
    private lateinit var mPhoto: CropImageView
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_employee)

        mPhoto = CropImageView(this)

        if (intent != null) {
            val dados = intent.getParcelableExtra("photo_employee") as Bitmap?
            photo_employee.setImageBitmap(dados)
        }

        listener()
    }

    private fun listener() {
        image_back.setOnClickListener(this)
        photo_employee.setOnClickListener(this)
        buttom_register_employee.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            image_back -> finish()
            photo_employee -> openPopUp()
            buttom_register_employee -> saveEmployee()
        }
    }

    private fun openPopUp() {
        val popMenu = PopupMenu(this, photo_employee)
        popMenu.menuInflater.inflate(R.menu.popup, popMenu.menu)
        popMenu.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.abrir_camera -> permissionCamera()
                R.id.abrir_galeria -> openGaleria()
            }
            true
        }
        popMenu.show()
    }

    private fun permissionCamera(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) ==
            PackageManager.PERMISSION_DENIED
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
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

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Nova Foto")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Foto Camera")

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val extras = data!!.extras!!["data"] as Bitmap
            photo_employee.setImageBitmap(extras)
        }
    }

    private fun openGaleria(): Boolean {
        return true
    }

    private fun saveEmployee() {
        val photo = mToByteArray.converterToByteArray(photo_employee)
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

        if (hora1 == "") {
            edit_horario1.error = "Horário Obrigatório"
        } else if (hora2 == "") {
            edit_horario2.error = "Horário Obrigatório"
        } else if (hora3 == "") {
            edit_horario3.error = "Horário Obrigatório"
        } else if (hora4 == "") {
            edit_horario4.error = "Horário Obrigatório"
        } else if (name == "") {
            edit_name.error = "Digite Nome"
        } else if (email == "") {
            edit_email.error = "Digite Email"
        } else if (cargo == "") {
            edit_cargo.error = "Digite Cargo"
        } else if (phone == "") {
            edit_phone.error = "Digite Telefone"
        } else if (admissao == "") {
            edit_admissao.error = "Digite Admissão"
        } else if (aniversario == "") {
            edit_aniversario.error = "Digite Aniversário"
        } else if (mBusinessEmployee.registerEmployee(
                photo, hora1, hora2, hora3, hora4, name, cargo,
                email, phone, admissao, aniversario
            )
        ) Toast.makeText(this, R.string.cadastro_feito, Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(this, "Não foi possível fazer o cadastro!", Toast.LENGTH_SHORT).show()
        }
    }
}