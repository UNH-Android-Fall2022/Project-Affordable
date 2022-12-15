package com.affordable.ui.main

import android.view.View
import android.view.WindowManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.affordable.R
import com.affordable.base.BaseActivity
import com.affordable.databinding.ActivityMainBinding
import com.affordable.ui.start.StartActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val TAG = StartActivity::class.java.name

    private var bottomNavigationView: BottomNavigationView? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView

    override fun initBindingRef(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun initRoot(): View? = binding?.root

    override fun init() {

        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))

        navView = findViewById<NavigationView>(R.id.nav_view);

        val navController: NavController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration =
            AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.settingsFragment,
                R.id.cardsFragment,
                R.id.feedbackFragment,
                R.id.thanksFragment
            )
                .setOpenableLayout(drawerLayout)
                .build()

        setupActionBarWithNavController(navController, appBarConfiguration)

        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment,
                R.id.accountFragment,
                R.id.shoppingFragment,
                R.id.storesFragment,
                R.id.privacyPolicyFragment,
                R.id.shoppingSelectionFragment,
                R.id.choiceSelectionFragment,
                R.id.cardSelectionFragment-> hideNav()
                else -> showNav()
            }
        }

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigationView?.setupWithNavController(navController)

    }

    override fun initListener() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun showNav() {
        (findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar).visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun hideNav() {
//        (findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar).visibility = View.GONE
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        bottomNavigationView?.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
            .apply {
                setStatusBarBackground(R.color.primaryDarkColor)
            }
    }

}