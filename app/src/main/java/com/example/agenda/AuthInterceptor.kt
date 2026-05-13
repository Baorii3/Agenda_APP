package com.example.agenda

import android.content.Context
import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthInterceptor(private val authManager: AuthManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        if (path == "/salas" || path == "/activitats") {
            return chain.proceed(request)
        }

        val token = runBlocking {
            suspendCoroutine<String?> { continuation ->
                authManager.checkSessionAndGetToken { tokenConBearer ->
                    continuation.resume(tokenConBearer)
                }
            }
        }
        Log.d("AuthInterceptor", "Token: $token")

        val newRequest = if (token != null) {
            request.newBuilder()
                .header("Authorization", token)
                .build()
        } else {
            request
        }
        Log.d("AuthInterceptor", "New request: $newRequest")
        return chain.proceed(newRequest)
    }
}