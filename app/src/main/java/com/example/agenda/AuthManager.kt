package com.example.agenda

import android.app.Activity
import android.content.Context
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthProvider
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.core.Amplify

class AuthManager {
    fun inicializarAmplify(context: Context) {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(context)
            Log.i("AuthManager", "Amplify configurado correctamente")
        } catch (error: AmplifyException) {
            Log.w("AuthManager", "Amplify ya estaba configurado, continuamos.")        }
    }

    fun loginWithGoogle(activity: Activity, onSuccess: () -> Unit) {
        Amplify.Auth.signInWithSocialWebUI(
            AuthProvider.google(), activity,
            { result ->
                Log.i("AuthManager", "Sign in succeeded: $result")
                onSuccess()
            },
            { error -> Log.e("AuthManager", "Sign in failed", error) }
        )
    }

    fun logOut(onSuccess: () -> Unit) {
        Amplify.Auth.signOut { result ->
            Log.i("AuthManager", "Resultado de cerrar sesión: $result")
            onSuccess()
        }
    }

    fun checkSessionAndGetToken(onTokenReceived: (String?) -> Unit) {
        Amplify.Auth.fetchAuthSession(
            { sessionResult ->
                val session = sessionResult as AWSCognitoAuthSession
                val tokens = session.userPoolTokensResult.value
                val idToken = tokens?.idToken
                Log.d("AuthManager", "ID Token obtenido: $idToken")
                if (idToken != null) {
                    onTokenReceived("Bearer $idToken")
                } else {
                    onTokenReceived(null)
                }
            },
            { error ->
                Log.e("AuthManager", "Fallo al obtener la sesión", error)
                onTokenReceived(null)
            }
        )
    }
}