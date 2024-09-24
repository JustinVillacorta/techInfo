package com.example.techinfo.MainNavigation
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.techinfo.Fragments.Admin.Admin

import com.example.techinfo.Fragments.Bottleneck.BottleNeck
import com.example.techinfo.Fragments.BuilcPC.BuildPC
import com.example.techinfo.Fragments.PcComparison.PcComparison
import com.example.techinfo.Fragments.Troubleshoot.TroubleShoot
import com.example.techinfo.R
import com.google.android.material.navigation.NavigationView
// Main Navigation Drawer For Tech Info
class MainNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            replaceFragment(BuildPC())
            navigationView.setCheckedItem(R.id.nav_buildpc)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_buildpc -> replaceFragment(BuildPC())
            R.id.nav_pc_compare -> replaceFragment(PcComparison())
            R.id.nav_bottleneck -> replaceFragment(BottleNeck())
            R.id.nav_troubleshoot -> replaceFragment(TroubleShoot())
            R.id.nav_admin -> replaceFragment(Admin())
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}