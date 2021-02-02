package com.example.app_point.activitys

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.app_point.R
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.activity_tools.*

class PerfilActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        listener()
        photoCircle()
    }

    private fun listener(){
        image_back_perfil.setOnClickListener(this)
        edit_employee.setOnClickListener(this)
    }

    private fun photoCircle(){
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.image_exemplo)
        val bitmapRound = RoundedBitmapDrawableFactory.create(resources, bitmap)
        bitmapRound.cornerRadius = 1000f
        image_photo_employee.setImageDrawable(bitmapRound)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_perfil -> finish()
            edit_employee -> startActivity(Intent(this, RegisterEmployeeActivity::class.java))
        }
    }

}