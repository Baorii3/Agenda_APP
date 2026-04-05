package com.example.agenda

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.core.Amplify
import com.example.agenda.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Amplify.addPlugin(com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("AuthQuickstart", "Amplify configurado correctamente")
        } catch (error: AmplifyException) {
            Log.e("AuthQuickstart", "Error al configurar Amplify", error)
        }
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }*/

        val toogle = ActionBarDrawerToggle(
            this, binding.drawerLayout,binding.toolbar, R.string.open_drawer, R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toogle)
        toogle.drawerArrowDrawable.color = resources.getColor(R.color.white, theme)
        toogle.syncState()

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }
                R.id.nav_login -> {
                    Amplify.Auth.signInWithSocialWebUI(
                        AuthProvider.google(),this,
                        { result -> Log.i("AuthQuickstart", "Sign in succeeded: $result") },
                        { error -> Log.e("AuthQuickstart", "Sign in failed", error) }
                    )
                }

            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }
}