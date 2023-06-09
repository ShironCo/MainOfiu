package com.example.ofiu.remote.apis.gpt

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer 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")
        return chain.proceed(request.build())
    }
}