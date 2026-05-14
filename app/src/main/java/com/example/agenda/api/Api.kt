package com.example.agenda.api

import android.content.Context
import com.example.agenda.AuthInterceptor
import com.example.agenda.AuthManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class Api {
    companion object {

        private var mRutinaApi: Retrofit? = null
        private val authManager = AuthManager()
        @Synchronized
        fun getSalaService(): SalaService {
            return getRetrofit().create(SalaService::class.java)
        }

        @Synchronized
        fun getActivitatService(): ActivitatService {
            return getRetrofit().create(ActivitatService::class.java)
        }

        @Synchronized
        fun getUsuariService(): UsuariService {
            return getRetrofit().create(UsuariService::class.java)
        }

        @Synchronized
        fun getDiccionariService(): DiccionariApiService {
            return getRetrofit().create(DiccionariApiService::class.java)
        }

        @Synchronized
        fun getDispositiuService(): DispositiuService {
            return getRetrofit().create(DispositiuService::class.java)
        }

        fun init(context: Context) {
            if (mRutinaApi == null) {
                authManager.inicializarAmplify(context)

                val gsondateformat = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

                mRutinaApi = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                    .baseUrl("https://api.agenda.ianordonez.cat/")
                    .build()
            }
        }

        private fun getRetrofit(): Retrofit {
            return mRutinaApi!!
        }


    }
}