package com.liquotrack.stocksip.shared.data.local

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // Skip Authorization for auth endpoints
        if (url.contains("/auth/google") || url.contains("/sign-in") || url.contains("/sign-up")) {
            Log.d("AuthInterceptor", "Skipping Authorization for auth endpoint. url=$url")
            return chain.proceed(originalRequest)
        }

        val token = tokenManager.getToken()

        if (token.isNullOrEmpty()) {
            Log.d("AuthInterceptor", "No token present. Proceeding without Authorization. url=${originalRequest.url}")
            return chain.proceed(originalRequest)
        }

        val masked = if (token.length > 10) token.take(6) + "..." + token.takeLast(4) else "***"
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        Log.d("AuthInterceptor", "Added Authorization header. token=$masked url=${originalRequest.url}")

        return chain.proceed(newRequest)
    }
}