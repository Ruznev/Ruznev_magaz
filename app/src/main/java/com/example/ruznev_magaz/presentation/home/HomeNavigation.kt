package com.example.ruznev_magaz.presentation.home

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ruznev_magaz.R
import com.example.ruznev_magaz.data.DbHelper
import com.example.ruznev_magaz.databinding.HomeNavigationScreenBinding
import com.example.ruznev_magaz.presentation.Authorization
import com.example.ruznev_magaz.presentation.home.fragments.CartFragment
import com.example.ruznev_magaz.presentation.home.fragments.HomeFragment
import com.example.ruznev_magaz.presentation.home.fragments.ItemFragment
import com.example.ruznev_magaz.presentation.home.fragments.ProfileFragment
import com.example.ruznev_magaz.presentation.home.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView

class HomeNavigation : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: HomeNavigationScreenBinding
    private lateinit var userEmail: String
    private lateinit var userLogin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = HomeNavigationScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        userLogin = sharedPreferences.getString("login", "") ?: ""
        userEmail = sharedPreferences.getString("email", "") ?: ""

        updateNavigationHeader(userLogin,userEmail)

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.bottom_Profile -> openFragment(ProfileFragment())
                R.id.bottom_Cart -> openFragment(CartFragment())
                R.id.bottom_Settings -> openFragment(SettingsFragment())
            }
            true
        }

        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())

        binding.fab.setOnClickListener() {
            Toast.makeText(this, "+", Toast.LENGTH_SHORT).show()
        }

        binding.fab.setOnClickListener() {
            openFragment(ItemFragment())
        }

    }

    fun updateNavigationHeader(login: String, email: String) {
        val headerView = binding.navigationView.getHeaderView(0)
        val headerLogin = headerView.findViewById<TextView>(R.id.drawer_header_login)
        val headerEmail = headerView.findViewById<TextView>(R.id.drawer_header_email)
        val headerImage = headerView.findViewById<ShapeableImageView>(R.id.drawer_header_image)

        headerLogin.text = login
        headerEmail.text = email

        val dbHelper = DbHelper(this, null)
        val bitmap = dbHelper.loadProfileImage(email)
        if (bitmap != null) {
            headerImage.setImageBitmap(bitmap)
        } else {
            headerImage.setImageResource(R.drawable.icons_default_avatar)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.Home -> openFragment(HomeFragment())
            R.id.Profile -> openProfileFragment()
            R.id.Cart -> openFragment(CartFragment())
            R.id.Settings -> openFragment(SettingsFragment())
            R.id.logout -> logoutUser()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun openProfileFragment() {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val login = sharedPreferences.getString("login", "")?:""
        val email = sharedPreferences.getString("email","")?:""

        val profileFragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("login",login)
                putString("email",email)
            }
        }
        openFragment(profileFragment)
    }

    private fun logoutUser() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle("Подтверждение")
            setMessage("Вы уверены, что хотите выйти из аккаунта?")
            setPositiveButton("Да") { dialog,which ->
                Toast.makeText(this@HomeNavigation,"Выходим из аккаунта...",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@HomeNavigation,Authorization::class.java))
                finish()
            }
            setNegativeButton("Отмена") {dialog, which ->
                dialog.cancel()
            }
            show()
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment,fragment)
        fragmentTransaction.commit()
        }


}
