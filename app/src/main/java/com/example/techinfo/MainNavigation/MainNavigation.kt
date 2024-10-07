package com.example.techinfo.MainNavigation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.techinfo.Fragments.Admin.Admin
import com.example.techinfo.Fragments.Admin.AdminView
import com.example.techinfo.Fragments.Admin.TroubleshootAdmin
import com.example.techinfo.Fragments.Bottleneck.BottleNeck
import com.example.techinfo.Fragments.BuildPC.BuildPC
import com.example.techinfo.Fragments.PcComparison.PartsComparison
import com.example.techinfo.Fragments.PcComparison.PcComparison
import com.example.techinfo.Fragments.Troubleshoot.troubleshoot_content.TroubleShoot
import com.example.techinfo.R
import com.google.android.material.navigation.NavigationView
import com.example.techinfo.databinding.ActivityMainNavigationBinding

// Main Navigation Drawer For Tech Info
class MainNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Admin.PassInt {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var binding: ActivityMainNavigationBinding // Assuming you're using View Binding
    var recievedData: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.buildcompare -> {
                    replaceFragment(PcComparison())
                    true
                }
                R.id.parts -> {
                    replaceFragment(PartsComparison())
                    true
                }

                R.id.pc_parts -> {
                    replaceFragment(AdminView())
                    true
                }
                R.id.troubleShoot_admin -> {
                    replaceFragment(TroubleshootAdmin())
                    true
                }
                else -> false
            }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Initialize with BuildPC fragment and hide bottom navigation
        if (savedInstanceState == null) {
            replaceFragment(BuildPC())
            navigationView.setCheckedItem(R.id.nav_buildpc)
            binding.bottomNavigationView.visibility = View.GONE // Hide Bottom Nav initially
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_buildpc -> {
                replaceFragment(BuildPC())
                binding.bottomNavigationView.visibility = View.GONE // Hide Bottom Nav
            }
            R.id.nav_pc_compare -> {
                replaceFragment(PcComparison())
                updateBottomNavigation(R.menu.bottom_pccompare_navbar) // Update Bottom Nav
            }
            R.id.nav_bottleneck -> {
                replaceFragment(BottleNeck())
                binding.bottomNavigationView.visibility = View.GONE // Hide Bottom Nav
            }
            R.id.nav_troubleshoot -> {
                replaceFragment(TroubleShoot())
                binding.bottomNavigationView.visibility = View.GONE // Hide Bottom Nav
            }
            R.id.nav_admin -> {
                if (recievedData == 1) {
                    // User logged in, show AdminView and bottom navigation
                    replaceFragment(AdminView())
                    binding.bottomNavigationView.visibility = View.VISIBLE // Show Bottom Nav after login
                    updateBottomNavigation(R.menu.bottom_navbar) // Show Admin specific nav menu
                } else {
                    // Not logged in, show Admin fragment and hide bottom navigation
                    replaceFragment(Admin())
                    binding.bottomNavigationView.visibility = View.GONE // Ensure Bottom Nav is hidden
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Method to update the bottom navigation menu dynamically
    private fun updateBottomNavigation(menuRes: Int) {
        binding.bottomNavigationView.menu.clear() // Clear existing menu items
        binding.bottomNavigationView.inflateMenu(menuRes) // Inflate new menu
        binding.bottomNavigationView.visibility = View.VISIBLE // Show Bottom Nav
    }

    // Interface method to receive data from Admin login process
    override fun PassInt(data: Int) {
        recievedData = data
        if (recievedData == 1) {
            // After login, replace fragment with AdminView and show the bottom navigation
            replaceFragment(AdminView())
            binding.bottomNavigationView.visibility = View.VISIBLE // Show Bottom Nav after login
            updateBottomNavigation(R.menu.bottom_navbar) // Show Admin specific nav menu
        }
    }

    // Helper method to replace fragments in the container
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Close navigation drawer when back button is pressed
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
