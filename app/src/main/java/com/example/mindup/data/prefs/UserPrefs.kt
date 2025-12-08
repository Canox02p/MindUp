package com.example.mindup.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
        val AUTH_TOKEN = stringPreferencesKey("auth_token")

        // 🔹 NUEVOS CAMPOS PARA LA MATERIA SELECCIONADA
        val SELECTED_MATERIA_NAME = stringPreferencesKey("selected_materia_name")
        val SELECTED_MATERIA_ID   = intPreferencesKey("selected_materia_id")
    }

    // ====== Sesión ======
    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.LOGGED_IN] ?: false }

    suspend fun setLoggedIn(value: Boolean) {
        context.dataStore.edit { it[Keys.LOGGED_IN] = value }
    }

    // Token
    val authToken: Flow<String?> =
        context.dataStore.data.map { it[Keys.AUTH_TOKEN] }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { it[Keys.AUTH_TOKEN] = token }
    }

    // ====== Cuenta ======
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

    // ====== MATERIA SELECCIONADA ======

    // Nombre (para el título "Mis Cursos" / "Álgebra", etc.)
    val selectedMateriaName: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.SELECTED_MATERIA_NAME] ?: "Mis Cursos"
        }

    // ID (si luego lo necesitas para llamadas a la API)
    val selectedMateriaId: Flow<Int?> =
        context.dataStore.data.map { prefs ->
            prefs[Keys.SELECTED_MATERIA_ID]
        }

    suspend fun saveSelectedMateria(nombre: String, id: Int) {
        context.dataStore.edit {
            it[Keys.SELECTED_MATERIA_NAME] = nombre
            it[Keys.SELECTED_MATERIA_ID] = id
        }
    }

    suspend fun clearAccount() {
        context.dataStore.edit {
            it.remove(Keys.ALIAS)
            it.remove(Keys.EMAIL)
            it.remove(Keys.PASSWORD)
            it.remove(Keys.AUTH_TOKEN)

            it.remove(Keys.SELECTED_MATERIA_NAME)
            it.remove(Keys.SELECTED_MATERIA_ID)

            it[Keys.LOGGED_IN] = false
        }
    }
}
