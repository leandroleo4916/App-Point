package com.example.app_point.activitys

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_point.R
import com.example.app_point.activitys.ui.profile.ProfileFragment
import com.example.app_point.activitys.ui.register.RegisterFragment
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.interfaces.ItemEmployee
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivityController : AppCompatActivity(), ItemEmployee {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_controller)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    override fun openFragmentProfile(employee: EmployeeEntity) {

        val navigationView = findViewById<View>(R.id.nav_view) as BottomNavigationView
        navigationView.selectedItemId = R.id.navigation_profile

        val args = Bundle()
        args.putSerializable("employee", employee)

        val fragment = ProfileFragment.newInstance()
        fragment.arguments = args

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_perfil, fragment, "profile")
            .addToBackStack(null)
            .commit()
    }

    override fun openFragmentRegister(id: Int) {
        val navigationView = findViewById<View>(R.id.nav_view) as BottomNavigationView
        navigationView.selectedItemId = R.id.navigation_register

        val args = Bundle()
        args.putSerializable("id", id)

        val fragment = RegisterFragment.newInstance()
        fragment.arguments = args

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_register, fragment, "register")
            .addToBackStack(null)
            .commit()
    }
}