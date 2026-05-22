package com.example.agenda

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amplifyframework.core.Amplify
import com.example.agenda.databinding.ActivityMainBinding
import com.example.agenda.fragments.DispositiuFragment
import com.example.agenda.fragments.HomeFragment
import com.example.agenda.fragments.PerfilFragment
import com.example.agenda.fragments.ReservasFragment
import com.example.agenda.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModelUsuari: UserViewModel by viewModels<UserViewModel>()

    // Instancia de AuthManager
    private val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Intentamos iniciar amplify
        authManager.inicializarAmplify(applicationContext)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        val toogle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.drawerArrowDrawable.color = resources.getColor(R.color.white, theme)
        toogle.syncState()

        val navView = binding.navView
        val headerView = if (navView.headerCount > 0) {
            navView.getHeaderView(0)
        } else {
            val hv = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navView, false)
            navView.addHeaderView(hv)
            hv
        }

        val tvHeaderUser = headerView.findViewById<TextView>(R.id.nav_header_username)
        val tvHeaderEmail = headerView.findViewById<TextView>(R.id.nav_header_email)

        checkUserSession()

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }
                R.id.nav_logout -> {
                    authManager.logOut { viewModelUsuari.setUser(null) }
                }
                R.id.nav_reservations -> {
                    if (viewModelUsuari.canAccessReservations()) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, ReservasFragment())
                            .commit()
                    }
                }
                R.id.nav_devices -> {
                    if (viewModelUsuari.canAccessDevices()) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, DispositiuFragment())
                            .commit()
                    }
                }
                R.id.nav_usuarios -> {
                    if (viewModelUsuari.canAccessUsers()) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, com.example.agenda.fragments.UsuariFragment())
                            .commit()
                    }
                }
                R.id.nav_login -> {
                    authManager.loginWithGoogle(this) {
                        checkUserSession()
                    }
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PerfilFragment())
                        .commit()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        viewModelUsuari.user.observe(this) { user ->
            val isLoggedIn = user != null
            val menu = binding.navView.menu

            menu.findItem(R.id.nav_logout).isVisible = isLoggedIn
            menu.findItem(R.id.nav_login).isVisible = !isLoggedIn
            menu.findItem(R.id.nav_reservations).isVisible = viewModelUsuari.canAccessReservations()
            menu.findItem(R.id.nav_devices).isVisible = viewModelUsuari.canAccessDevices()
            menu.findItem(R.id.nav_usuarios).isVisible = viewModelUsuari.canAccessUsers()

            if (user != null) {
                tvHeaderUser.text = user.nom
                tvHeaderEmail.text = user.email
            } else {
                tvHeaderUser.text = getString(R.string.menu_login)
                tvHeaderEmail.text = ""
            }

            applyAuthMenuStyles()
        }
    }

    private fun applyAuthMenuStyles() {
        val menu = binding.navView.menu
        tintMenuItem(menu.findItem(R.id.nav_login), R.color.menu_login)
        tintMenuItem(menu.findItem(R.id.nav_logout), R.color.menu_logout)
    }

    private fun tintMenuItem(item: android.view.MenuItem?, colorResId: Int) {
        if (item == null) return
        val color = ContextCompat.getColor(this, colorResId)
        item.title = SpannableString(item.title).apply {
            setSpan(ForegroundColorSpan(color), 0, length, 0)
        }
        item.icon?.mutate()?.setTint(color)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.data?.scheme == "agenda-app") {
            Amplify.Auth.handleWebUISignInResponse(intent)
        }
    }

    fun checkUserSession() {
        authManager.checkSessionAndGetToken { authHeader ->
            if (authHeader != null) {
                viewModelUsuari.fetchUserData(
                    authHeader,
                    onAuthError = { authManager.logOut { viewModelUsuari.setUser(null) } },
                    onMessage = { showToast(it) }
                )
            } else {
                viewModelUsuari.setUser(null)
                showToast(getString(R.string.menu_login))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}