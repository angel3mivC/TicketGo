package mx.tec.ticketgo.data.network

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import mx.tec.ticketgo.App
import mx.tec.ticketgo.data.network.TokenStorage.TOKEN_KEY
import mx.tec.ticketgo.dataStore

object TokenStorage {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")

    suspend fun saveToken(token: String) {
        App.instance.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        val prefs = App.instance.dataStore.data.first()
        val token = prefs[TOKEN_KEY]
        println("🔑 TOKEN OBTENIDO: $token")
        return token
    }

    suspend fun saveUserName(userName: String) {
        App.instance.dataStore.edit { prefs ->
            prefs[USER_NAME_KEY] = userName
        }
    }

    suspend fun getUserName(): String? {
        val prefs = App.instance.dataStore.data.first()
        val userName = prefs[USER_NAME_KEY]
        println("👤 NOMBRE DE USUARIO OBTENIDO: $userName")
        return userName
    }

    suspend fun clearToken() {
        App.instance.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(USER_NAME_KEY)
        }
    }
}
