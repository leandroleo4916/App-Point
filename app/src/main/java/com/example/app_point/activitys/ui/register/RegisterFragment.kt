package com.example.app_point.activitys.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app_point.R

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View {

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_register, container, false)

        return root
    }
}