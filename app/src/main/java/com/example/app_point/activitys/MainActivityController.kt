package com.example.app_point.activitys

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_point.R
import com.example.app_point.activitys.ui.home.HomeFragment
import com.example.app_point.activitys.ui.profile.ProfileFragment
import com.example.app_point.activitys.ui.register.RegisterFragment
import com.example.app_point.constants.ConstantsEmployee
import com.example.app_point.entity.EmployeeEntity
import com.example.app_point.interfaces.ItemEmployee
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.lang.Float.max
import java.lang.Float.min

class MainActivityController : AppCompatActivity(), ItemEmployee {

    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_controller)

        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        showNavView()
    }

    override fun openFragmentProfile(employee: EmployeeEntity) {

        navView.selectedItemId = R.id.navigation_profile
        val args = Bundle()
        args.putSerializable(ConstantsEmployee.EMPLOYEE.TABLE_NAME, employee)

        val fragmentHome = HomeFragment.newInstance()
        val fragment = ProfileFragment.newInstance()
        fragment.arguments = args

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_perfil, fragment, "profile")
            .addToBackStack(null)
            .detach(fragmentHome)
            .commit()
    }

    override fun openFragmentRegister(id: Int) {

        navView.selectedItemId = R.id.navigation_register
        val args = Bundle()
        args.putSerializable("id", id)

        val fragmentHome = HomeFragment.newInstance()
        val fragment = RegisterFragment.newInstance()
        fragment.arguments = args

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_register, fragment, "register")
            .addToBackStack(null)
            .detach(fragmentHome)
            .commit()
    }

    private fun showNavView(){

        var isShow = true
        var scrollRange = -1
        val appBar = findViewById<AppBarLayout>(R.id.appbar)
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                animateBarVisibility(navView, false)
                isShow = true
            } else if (isShow){
                animateBarVisibility(navView, true)
                isShow = false
            }
        })
    }

    private fun animateBarVisibility(child: View, isVisible: Boolean) {

        var offsetAnimator: ValueAnimator? = null

        if (offsetAnimator == null) {
            offsetAnimator = ValueAnimator().apply {
                interpolator = DecelerateInterpolator()
                duration = 150L
            }

            offsetAnimator.addUpdateListener {
                child.translationY = it.animatedValue as Float
            }
        } else {
            offsetAnimator.cancel()
        }

        val targetTranslation = if (isVisible) 0f else child.height.toFloat()
        offsetAnimator.setFloatValues(child.translationY, targetTranslation)
        offsetAnimator.start()
    }
}

class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    private var lastStartedType: Int = 0
    private var offsetAnimator: ValueAnimator? = null
    private var isSnappingEnabled = false

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        if (dependency is Snackbar.SnackbarLayout) { updateSnackBar(child, dependency) }
        return super.layoutDependsOn(parent, child, dependency)
    }

    override fun onStartNestedScroll (coordinatorLayout: CoordinatorLayout, child: V,
                                      directTargetChild: View, target: View,
                                      axes: Int, type: Int, ): Boolean {

        if (axes != ViewCompat.SCROLL_AXIS_VERTICAL) return false

        lastStartedType = type
        offsetAnimator?.cancel()

        return true
    }

    override fun onNestedPreScroll (coordinatorLayout: CoordinatorLayout, child: V,
                                    target: View, dx: Int, dy: Int, consumed: IntArray,
                                    type: Int, ) {

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }

    override fun onStopNestedScroll (coordinatorLayout: CoordinatorLayout, child: V,
                                     target: View, type: Int) {
        if (!isSnappingEnabled) return

        if (lastStartedType == ViewCompat.TYPE_TOUCH || type == ViewCompat.TYPE_NON_TOUCH) {

            val currTranslation = child.translationY
            val childHalfHeight = child.height * 0.5f

            // translate down
            if (currTranslation >= childHalfHeight) {
                animateBarVisibility(child, isVisible = false)
            }
            // translate up
            else { animateBarVisibility(child, isVisible = true) }
        }
    }

    private fun animateBarVisibility(child: View, isVisible: Boolean) {
        if (offsetAnimator == null) {
            offsetAnimator = ValueAnimator().apply {
                interpolator = DecelerateInterpolator()
                duration = 150L
            }

            offsetAnimator?.addUpdateListener {
                child.translationY = it.animatedValue as Float
            }
        } else { offsetAnimator?.cancel() }

        val targetTranslation = if (isVisible) 0f else child.height.toFloat()
        offsetAnimator?.setFloatValues(child.translationY, targetTranslation)
        offsetAnimator?.start()
    }

    private fun updateSnackBar(child: View, snackbarLayout: Snackbar.SnackbarLayout) {
        if (snackbarLayout.layoutParams is CoordinatorLayout.LayoutParams) {
            val params = snackbarLayout.layoutParams as CoordinatorLayout.LayoutParams

            params.anchorId = child.id
            params.anchorGravity = Gravity.TOP
            params.gravity = Gravity.TOP
            snackbarLayout.layoutParams = params
        }
    }
}