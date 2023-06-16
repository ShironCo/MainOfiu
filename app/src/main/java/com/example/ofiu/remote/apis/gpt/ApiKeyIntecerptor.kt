package com.example.ofiu.remote.apis.gpt

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // Obtener la solicitud original y crear una nueva instancia del constructor de la solicitud

        val request = chain.request().newBuilder()

        // Agregar el encabezado de autorización a la solicitud con la clave de API

        request.addHeader("Authorization", "Bearer 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")

        // Proceder con la cadena de interceptores y devolver la respuesta modificada
        return chain.proceed(request.build())
    }
}