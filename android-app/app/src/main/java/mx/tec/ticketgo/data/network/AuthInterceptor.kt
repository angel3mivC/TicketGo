package mx.tec.ticketgo.data.network

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { TokenStorage.getToken() }
        
        // Log temporal para diagnosticar
        println("🌐 URL: ${chain.request().url}")
        println("🔑 Token disponible: ${token != null}")

        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        
        return chain.proceed(request)
    }
}
