package com.example.mindup.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

data class AccountPrefs(
    val alias: String = "",
    val email: String = "",
    val password: String = ""
)

class UserPrefs(private val context: Context) {

    private object Keys {
        val LOGGED_IN = booleanPreferencesKey("logged_in")
        val ALIAS = stringPreferencesKey("alias")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
    }

    // Sesión
    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.LOGGED_IN] ?: false }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { it[Keys.LOGGED_IN] = value }
    }

    // Cuenta (alias, email, password)
    val account: Flow<AccountPrefs> =
        context.dataStore.data.map {
            AccountPrefs(
                alias = it[Keys.ALIAS] ?: "",
                email = it[Keys.EMAIL] ?: "",
                password = it[Keys.PASSWORD] ?: ""
            )
        }

    suspend fun setAccount(alias: String, email: String, password: String) {
        context.dataStore.edit {
            it[Keys.ALIAS] = alias
            it[Keys.EMAIL] = email
            it[Keys.PASSWORD] = password
        }
    }

    suspend fun clearAccount() {
        context.dataStore.edit {
            it.remove(Keys.ALIAS)
            it.remove(Keys.EMAIL)
            it.remove(Keys.PASSWORD)
            it[Keys.LOGGED_IN] = false
        }
    }
}
