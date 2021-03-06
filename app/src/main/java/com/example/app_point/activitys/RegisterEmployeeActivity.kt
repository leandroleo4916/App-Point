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
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app_point.R
import com.example.app_point.business.BusinessEmployee
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.entity.Employee
import com.example.app_point.entity.EmployeeDados
import com.example.app_point.utils.ConverterPhoto
import com.example.app_point.entity.EmployeeEntity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_register_employee.*
import kotlinx.android.synthetic.main.activity_register_employee.edittext_email
import kotlinx.android.synthetic.main.activity_register_employee.edittext_username
import kotlinx.android.synthetic.main.activity_register_employee.image_back
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class RegisterEmployeeActivity : AppCompatActivity(), View.OnClickListener {

    private val mBusinessEmployee: BusinessEmployee = BusinessEmployee(this)
    private val mToByteArray: ConverterPhoto = ConverterPhoto()
    private lateinit var database: FirebaseDatabase
    private lateinit var fire: FirebaseFirestore
    private lateinit var stor: FirebaseStorage
    private val PERMISSION_CODE = 1000
    private val IMAGE_GALERY = 1
    private val IMAGE_CAPTURE_CODE = 1001
    private var image_uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_employee)

        database = Firebase.database
        stor = Firebase.storage
        fire = Firebase.firestore

        carregaInfoEmployee()
        inicialDate()
        listener()
    }

    private fun writeNewEmployee(employee: Employee) {

        val storageRef = stor.reference
        val imageRef = storageRef.child(employee.nameEmployee)
        val mImagesRef = storageRef.child("images/"+employee.nameEmployee)
        imageRef.name == mImagesRef.name
        imageRef.path == mImagesRef.path

        /*
        val uploadTask = mImagesRef.putBytes(employee.photo)
        uploadTask
            .addOnFailureListener {
                Toast.makeText(this, R.string.nao_foi_possivel_cadastrar, Toast.LENGTH_LONG).show()
            }
            .addOnSuccessListener {
                save(employee, mImagesRef, it)
            }
        */
        /*
        database = FirebaseDatabase.getInstance().getReference(ConstantsEmployee.EMPLOYEE.TABLE_NAME)

        database.child(ConstantsEmployee.EMPLOYEE.COLUMNS.NAME).setValue(employee)
            .addOnSuccessListener {
                Toast.makeText(this, R.string.cadastro_feito, Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this, ProfileActivity::class.java))
                //finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.nao_foi_possivel_cadastrar), Toast.LENGTH_SHORT).show()
            }
         */
    }

    private fun save(employee: Employee, img: StorageReference, task: UploadTask.TaskSnapshot) {

        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(img.toString())
        val st = storageRef.child(img.toString()).downloadUrl
        val gsReference = stor.getReferenceFromUrl(img.toString())
        //val firebase = Firebase.database.reference
        gsReference.downloadUrl.addOnSuccessListener {
            val it1 = it.isHierarchical
        }
        val up = task.uploadSessionUri

        val dados = EmployeeDados("", employee.horario1, employee.horario2, employee.horario3,
            employee.horario4, employee.nameEmployee, employee.emailEmployee, employee.cargoEmployee,
            employee.phoneEmployee, employee.admissaoEmployee, employee.aniversarioEmployee)

        fire.collection("funcionários")
            .add(dados)
            .addOnSuccessListener {

                Toast.makeText(this, R.string.cadastro_feito, Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ToolsActivity::class.java))
                finish()

            }
            .addOnFailureListener{
                Toast.makeText(this, R.string.nao_foi_possivel_cadastrar, Toast.LENGTH_LONG).show()
            }

        /*
        firebase.child("funcionarios").child(employee.phoneEmployee)
            .setValue(dados)
            .addOnSuccessListener { Toast.makeText(this, R.string.cadastro_feito,
                Toast.LENGTH_LONG).show() }
            .addOnFailureListener { Toast.makeText(this, R.string.nao_foi_possivel_cadastrar,
                Toast.LENGTH_LONG).show()
            }*/
    }

    private fun listener() {
        image_back.setOnClickListener(this)
        photo_employee.setOnClickListener(this)
        buttom_register_employee.setOnClickListener(this)
        horario1.setOnClickListener(this)
        horario2.setOnClickListener(this)
        horario3.setOnClickListener(this)
        horario4.setOnClickListener(this)
        text_admissao.setOnClickListener(this)
        text_aniversario.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            image_back -> finish()
            photo_employee -> openPopUp()
            buttom_register_employee -> extrasId()
            horario1 -> timePicker(1)
            horario2 -> timePicker(2)
            horario3 -> timePicker(3)
            horario4 -> timePicker(4)
            text_admissao -> calendar(1)
            text_aniversario -> calendar(2)
        }
    }

    // Captures date and show in the EditText date and hour
    @SuppressLint("WeekBasedYear")
    private fun inicialDate(){
        val date = getInstance().time
        val dateTime = SimpleDateFormat("dd/MM/YYYY", Locale.ENGLISH)
        val dataCurrent = dateTime.format(date)
        text_admissao.text = dataCurrent
        text_aniversario.text = dataCurrent
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
                1 -> text_admissao.text = SimpleDateFormat("dd/MM/YYYY").format(date.time)
                2 -> text_aniversario.text = SimpleDateFormat("dd/MM/YYYY").format(date.time)
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
                1 -> { horario1.text = SimpleDateFormat("HH:mm").format(cal.time) }
                2 -> { horario2.text = SimpleDateFormat("HH:mm").format(cal.time) }
                3 -> { horario3.text = SimpleDateFormat("HH:mm").format(cal.time) }
                4 -> { horario4.text = SimpleDateFormat("HH:mm").format(cal.time) }
            }
        }
        TimePickerDialog(
            this, timeSetListener, cal.get(HOUR_OF_DAY),
            cal.get(MINUTE), true
        ).show()

    }

    // Direciona pelo id se será opção salvar ou editar funcionárop
    private fun extrasId() {
        val extras = intent.extras
        if (extras != null) {
            saveEmployee(extras.getInt(ConstantsEmployee.EMPLOYEE.COLUMNS.ID))
        } else {
            saveEmployee(id = 0)
        }
    }

    // Receive data to edition
    private fun carregaInfoEmployee() {

        val extras = intent.extras
        if (extras != null) {

            val id = extras.getInt(ConstantsEmployee.EMPLOYEE.COLUMNS.ID)
            val infoEmployee: EmployeeEntity = mBusinessEmployee.consultEmployeeWithId(id)!!
            val photo = mToByteArray.converterToBitmap(infoEmployee.photo)
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

    // Popup open camera or gallery
    private fun openPopUp() {
        val popMenu = PopupMenu(this, photo_employee)
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
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
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

            // Capture Image of the Gallery
            if (requestCode == IMAGE_GALERY) {
                val selectedImage: Uri? = data!!.data
                photo_employee.setImageURI(selectedImage)
            }
            // Capture Image of the Camera
            else if (resultCode == Activity.RESULT_OK) {
                val extras = data!!.extras!!["data"] as Bitmap
                photo_employee.setImageBitmap(extras)
            }
        }
    }

    // Open gallery
    private fun openGalery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_GALERY)
    }

    private fun saveEmployee(id: Int) {
        val image = photo_employee
        val photo = mToByteArray.converterToByteArray(image!!)
        val hora1 = horario1.text.toString()
        val hora2 = horario2.text.toString()
        val hora3 = horario3.text.toString()
        val hora4 = horario4.text.toString()
        val name = edittext_username.text.toString()
        val email = edittext_email.text.toString()
        val cargo = edittext_cargo.text.toString()
        val phone = edittext_phone.text.toString()
        val admissao = text_admissao.text.toString()
        val aniversario = text_aniversario.text.toString()

        val edit_name = edittext_username
        val edit_email = edittext_email
        val edit_cargo = edittext_cargo
        val edit_phone = edittext_phone

        when {
            name == "" -> edit_name.error = "Faltou mome"
            email == "" -> edit_email.error = "Faltou email"
            cargo == "" -> edit_cargo.error = "Faltou cargo"
            phone == "" -> edit_phone.error = "Faltou telefone"

            else -> saveTest (Employee(photo!!, hora1, hora2, hora3, hora4, name, email, cargo,
                phone, admissao, aniversario))
        }
    }

    private fun saveTest (employee: Employee) {

        fire.collection("funcionários")
            .add(employee)
            .addOnSuccessListener {

                Toast.makeText(this, R.string.cadastro_feito, Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ToolsActivity::class.java))
                finish()

            }
            .addOnFailureListener{
                Toast.makeText(this, R.string.nao_foi_possivel_cadastrar, Toast.LENGTH_LONG).show()
            }

    }
}