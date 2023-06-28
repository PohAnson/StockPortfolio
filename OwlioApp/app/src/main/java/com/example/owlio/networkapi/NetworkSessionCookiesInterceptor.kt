package com.example.owlio.networkapi

import com.example.owlio.data.OwlioDataStorePreferences
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class NetworkApiSessionCookiesInterceptor @Inject constructor(private val owlioDataStorePreferences: OwlioDataStorePreferences) :
    Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder().header("sassyid", owlioDataStorePreferences.sessionId)
            .method(request.method(), request.body()).build()
        return chain.proceed(newRequest)
    }
}