package com.example.agenda

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.core.Amplify
import com.example.agenda.api.Api
import com.example.agenda.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)
            Log.i("AuthQuickstart", "Amplify configurado correctamente")
        } catch (error: AmplifyException) {
            Log.w("AuthQuickstart", "Amplify ya estaba configurado, continuamos.")
        }
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
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
        Amplify.Auth.fetchAuthSession(
            { sessionResult ->
                val session = sessionResult as AWSCognitoAuthSession
                val isSignedIn = session.isSignedIn
                Log.i("AuthQuickstart", "Usuario logueado: $isSignedIn")
                actualizarMenu(isSignedIn)
            },
            { error -> Log.e("AuthQuickstart", "Fallo al obtener la sesión", error) }
        )

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }
                R.id.nav_logout -> {
                    Amplify.Auth.signOut { signOutResult ->
                        Log.i("AuthQuickstart", "Resultado de cerrar sesión: $signOutResult")
                        actualizarMenu(false)
                    }
                    binding.drawerLayout.closeDrawers()
                }
                R.id.nav_login -> {
                    Amplify.Auth.signInWithSocialWebUI(
                        AuthProvider.google(), this,
                        { result ->
                            Log.i("AuthQuickstart", "Sign in succeeded: $result")
                            Amplify.Auth.fetchAuthSession(
                                { sessionResult ->
                                    val session = sessionResult as AWSCognitoAuthSession
                                    val tokens = session.userPoolTokensResult.value

                                    if (tokens != null) {
                                        val accessToken = tokens.accessToken
                                        val idToken = tokens.idToken
                                        Log.i("MI_TOKEN", "Access Token: $accessToken")
                                        Log.i("MI_TOKEN", "ID Token: $idToken")
                                        if (idToken != null) {
                                            val authHeader = "Bearer $idToken"
                                            lifecycleScope.launch {
                                                try {
                                                    val response = Api.getUsuariService().crearUsuario(authHeader)

                                                    if (response.isSuccessful){
                                                        val usuario = response.body()
                                                        Log.i("API", "Usuario creado: $usuario")
                                                        actualizarMenu(true)
                                                    } else{
                                                        Log.e("API", "Error HTTP al crear usuario: ${response.code()}")
                                                        Amplify.Auth.signOut { actualizarMenu(false) }
                                                    }
                                                } catch (e: Exception) {
                                                    Log.e("API", "Error de conexión al crear usuario", e)
                                                }
                                            }
                                        }

                                    } else {
                                        Log.w("MI_TOKEN", "Sign in succeeded but no tokens found")
                                    }
                                },
                                { error -> Log.e("MI_TOKEN", "Fallo al obtener la sesión", error) }
                            )
                        },
                        { error -> Log.e("AuthQuickstart", "Sign in failed", error) }
                    )
                }

            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.data?.scheme == "agenda-app"){
            Amplify.Auth.handleWebUISignInResponse(intent)
        }
    }

    fun actualizarMenu(estaLogueado: Boolean) {
        runOnUiThread {
            val menu = binding.navView.menu
            menu.findItem(R.id.nav_login)?.isVisible = !estaLogueado
            menu.findItem(R.id.nav_logout)?.isVisible = estaLogueado
        }
    }
}