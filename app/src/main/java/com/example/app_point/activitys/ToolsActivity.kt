package com.example.app_point.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.app_point.R
import kotlinx.android.synthetic.main.activity_tools.*

class ToolsActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)

        listener()
    }

    private fun listener(){
        image_back_tools.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            image_back_tools -> finish()
        }
    }

}