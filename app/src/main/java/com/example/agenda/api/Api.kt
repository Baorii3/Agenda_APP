package com.example.agenda.api

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

        // De momento no usamos ningun servicio, asi podemos elegir el que queramos
        private var mRutinaApi: Retrofit? = null

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

        private fun getRetrofit(): Retrofit {
            if (mRutinaApi == null) {
                // Es para las fechas, el formato que nos da el backend es "yyyy-MM-dd'T'HH:mm:ss"
                val gsondateformat = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

                val unsafeOkHttpClient = getUnsafeOkHttpClient()

                mRutinaApi = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                    .baseUrl("https://54.234.84.164/api/")
                    .client(unsafeOkHttpClient)
                    .build()
            }
            return mRutinaApi!!
        }

        private fun getUnsafeOkHttpClient(): OkHttpClient {
            try {
                // Crea un trust manager que NO valida certificats
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                )

                // Instal·la el trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                val sslSocketFactory = sslContext.socketFactory

                return OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier { _, _ -> true } // Accepta qualsevol hostname
                    .build()

            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}