package com.example.mindup.ui.viewmodel
import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Application.dataStore by preferencesDataStore("profile_prefs")

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val KEY_NAME = stringPreferencesKey("name")
    private val KEY_EMAIL = stringPreferencesKey("email")
    private val KEY_PHONE = stringPreferencesKey("phone")
    private val KEY_BIO = stringPreferencesKey("biografia")

    val name = app.dataStore.data.map { it[KEY_NAME] ?: "Nombre" }
    val email = app.dataStore.data.map { it[KEY_EMAIL] ?: "correo.@gmail.com" }
    val phone = app.dataStore.data.map { it[KEY_PHONE] ?: "123456789" }
    val bio = app.dataStore.data.map { it[KEY_BIO] ?: "escribe..." }

    fun save(name: String, email: String, phone: String, bio: String) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit {
                it[KEY_NAME] = name
                it[KEY_EMAIL] = email
                it[KEY_PHONE] = phone
                it[KEY_BIO] = bio
            }
        }
    }
}