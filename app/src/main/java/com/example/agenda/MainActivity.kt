package com.example.agenda

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
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
            this, binding.drawerLayout,binding.toolbar, R.string.open_drawer, R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.drawerArrowDrawable.color = resources.getColor(R.color.white, theme)
        toogle.syncState()

        checkUserSession()

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }
                R.id.nav_logout -> {
                    authManager.logOut{ viewModelUsuari.setUser(null) }
                }
                R.id.nav_reservations -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ReservasFragment())
                        .commit()
                }
                R.id.nav_devices -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, DispositiuFragment())
                        .commit()
                }
                R.id.nav_usuarios -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, com.example.agenda.fragments.UsuariFragment())
                        .commit()
                }
                R.id.nav_login -> {
                    authManager.loginWithGoogle(this){
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
            binding.navView.menu.findItem(R.id.nav_logout).isVisible = (user != null)
            binding.navView.menu.findItem(R.id.nav_login).isVisible = (user == null)

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.data?.scheme == "agenda-app"){
            Amplify.Auth.handleWebUISignInResponse(intent)
        }
    }
    fun checkUserSession() {
        authManager.checkSessionAndGetToken { authHeader ->
            if (authHeader != null) {
                viewModelUsuari.fetchUserData(authHeader) {
                    authManager.logOut { viewModelUsuari.setUser(null) }
                }
            } else {
                viewModelUsuari.setUser(null)
            }
        }
    }
}