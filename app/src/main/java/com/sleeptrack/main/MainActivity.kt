package com.sleeptrack.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.sleeptrack.R

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))

        navView = findViewById<NavigationView>(R.id.nav_view);

        val navController: NavController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration =
            AppBarConfiguration.Builder(
                R.id.SleepTrackerFragment,
            )
                .setOpenableLayout(drawerLayout)
                .build()

        setupActionBarWithNavController(navController, appBarConfiguration)

        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.recordSleepFragment -> hideNav()
                else -> showNav()
            }
        }

    }

    private fun showNav() {
        (findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar).visibility = View.VISIBLE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun hideNav() {
        (findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar).visibility = View.GONE
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
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