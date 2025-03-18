package com.dalmuina.portolioapp.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(
            "Accept", "application/json"
        ).build()
        return chain.proceed(request)
    }
}

fun getClient(): OkHttpClient =
    OkHttpClient
        .Builder()
        .addInterceptor(HeaderInterceptor())
        .build()
