package mx.tec.ticketgo.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { TokenStorage.getToken() }
        println("🪪 TOKEN ENVIADO: $token")
        println("🌐 URL DE LA PETICIÓN: ${chain.request().url}")
        println("📝 MÉTODO: ${chain.request().method}")

        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
            println("✅ HEADER Authorization agregado: Bearer $it")
        } ?: run {
            println("❌ NO HAY TOKEN - Header Authorization NO agregado")
        }

        val request = requestBuilder.build()
        println("📋 HEADERS FINALES: ${request.headers}")
        
        return chain.proceed(request)
    }
}
