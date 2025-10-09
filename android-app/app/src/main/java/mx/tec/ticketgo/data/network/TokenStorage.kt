package mx.tec.ticketgo.data.network

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import mx.tec.ticketgo.App
import mx.tec.ticketgo.data.network.TokenStorage.TOKEN_KEY
import mx.tec.ticketgo.dataStore

object TokenStorage {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    suspend fun saveToken(token: String) {
        App.instance.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        val prefs = App.instance.dataStore.data.first()
        return prefs[TOKEN_KEY]
    }

    suspend fun clearToken() {
        App.instance.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
}
