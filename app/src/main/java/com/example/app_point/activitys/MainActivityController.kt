package com.example.app_point.activitys

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_point.R
import com.example.app_point.activitys.ui.profile.ProfileFragment
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.interfaces.ItemEmployee
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivityController : AppCompatActivity(), ItemEmployee {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_controller)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_points,
            R.id.navigation_profile,
            R.id.navigation_employee,
            R.id.navigation_register
        ))

        //supportActionBar?.hide()
        navView.setupWithNavController(navController)
    }

    override fun openFragment(employee: EmployeeEntity) {

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
}