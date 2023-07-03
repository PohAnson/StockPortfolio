package com.example.owlio.networkapi

import com.example.owlio.data.OwlioDataStorePreferences
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import javax.inject.Inject

class NetworkApiSessionCookiesInterceptor @Inject constructor(private var owlioDataStorePreferences: OwlioDataStorePreferences) :
    Interceptor {
    override fun intercept(chain: Chain): Response {
        var request = chain.request()
        val sessionId = owlioDataStorePreferences.sessionId ?: ""
        request =
            request.newBuilder()
                .headers(Headers.of("Cookie", "sassyid=$sessionId"))
                .method(request.method(), request.body())
                .build()
        return chain.proceed(request)
    }
}