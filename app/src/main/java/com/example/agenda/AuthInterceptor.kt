package com.example.agenda

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // val token = prefs.getString("TOKEN", null)

        // eliminar la linea de abajo en el futuro
        val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjE5Y2FhZWNkZThmNDg1ZThmNTkzOGY0OGFiYTBjZTdhMzU4MWYwMjciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTI0MzI2MDQwMjUzODkwNjc1MDgiLCJoZCI6Iml0aWNiY24uY2F0IiwiZW1haWwiOiIyMjIzX2lhbi5vcmRvbmV6QGl0aWNiY24uY2F0IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJGM21TX2Rpc1kzTGt1M1ZNMWdTeUJnIiwibmFtZSI6IklhbiBPcmRvw7FleiBCYXJiYXJhIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0s3ZXJHdVNSQzZNMnVEWWlVMlJYRjBidlVwNndrVnVybGt2OGcwN2ZrQTlMSmd5Zz1zOTYtYyIsImdpdmVuX25hbWUiOiJJYW4iLCJmYW1pbHlfbmFtZSI6Ik9yZG_DsWV6IEJhcmJhcmEiLCJpYXQiOjE3Nzc0NzMxNjMsImV4cCI6MTc3NzQ3Njc2M30.JEOKtMhA_bHM-EQZikuiz79JbTG1c-m5CEAC8xhVw7yTwxB80yLupW4bYoPLlgOLp4RElBGeVMR47aPeUd2q9nngfVWZyrzpwqTnjGoDittv99V4LqyhFYsERF3s-GRZ5EpdATWp8-yqZ9zTB_8MyiRooYpzezvx4NilCoQ5Ome5KkDb73TqIDB96y0osyODNLWR5ETPCm_tkE3ih1lJZizhzVmfWg4f9WJI5Hv4NUUYQYOMAjoNd4SqSkIQo1GZ_ReQLn5W291y7_MGB6qO9hoYjEozChXpNrZRdthFBPdTAhrF3EIHJSBY8VsJ63jDnVTLnkQ1luQlJDrspc2qkg"
        Log.d("AuthInterceptor", "Token: $token")
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}