package com.example.app_point.activitys

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.app_point.R
import kotlinx.android.synthetic.main.activity_photo_perfil.*


class PhotoPerfilActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_perfil)

        val dados = intent.getParcelableExtra("photo_employee") as Bitmap?
        photo_activity.setImageBitmap(dados)

        listener()
    }

    private fun listener(){
        buttom_cortar_photo.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val photo = photo_activity.croppedImage
        when(view) {
            buttom_cortar_photo -> passandoImage(photo)
        }
    }

    fun passandoImage(photo: Bitmap){
        val intent = Intent(this, RegisterEmployeeActivity::class.java)
        intent.putExtra("photo_employee", photo)
        startActivity(intent)
        finish()
    }

}

