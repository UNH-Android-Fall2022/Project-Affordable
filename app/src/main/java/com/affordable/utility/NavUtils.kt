package com.affordable.utility

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

fun AppCompatActivity.setupNavigation(navigationView: BottomNavigationView, id: Int) {
    val navHostFragment =
        supportFragmentManager.findFragmentById(id) as NavHostFragment?
    if (navHostFragment != null) {
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(navigationView, navController)
    }
}

fun NavController.isNav(id: Int, itemClick: () -> Unit) {
    if (currentDestination != null && currentDestination!!.id == id)
        itemClick()
}


fun NavController.destinationToSpecific(fragmentId: Int, action: Int) {
    val navOptions = NavOptions.Builder().setPopUpTo(fragmentId, true).build()
    navigate(action, null, navOptions)
}

fun AppCompatActivity.destination(
    navHostFragmentId: Int,
    navGraphId: Int,
    destinationFragmentId: Int
) {
    val navHostFragment =
        (supportFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment)
    val inflater = navHostFragment.navController.navInflater
    val graph = inflater.inflate(navGraphId)
//graph.addArgument("argument", NavArgument)
    graph.setStartDestination(destinationFragmentId)
//or
//graph.startDestination = R.id.fragment2
    navHostFragment.navController.graph = graph
}


fun NavController.getNavigationResult(key: String = "result") =
    currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(key)

fun NavController.setNavigationResult(result: Boolean, key: String = "result") {
    previousBackStackEntry?.savedStateHandle?.set(key, result)
}




