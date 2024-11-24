package com.example.pig_marco_ramos.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "user_data_store"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreManager(private val context: Context) {

    companion object {
        private val USER_KEY = stringPreferencesKey("user")
        private val PASS_KEY = stringPreferencesKey("password")
    }

    suspend fun saveUser(name: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = name
            preferences[PASS_KEY] = password
        }
    }

    suspend fun clearPreferences() {
        context.dataStore.edit { prefereces ->
            prefereces[USER_KEY] = ""
            prefereces[PASS_KEY] = ""
        }
    }

    val userData: Flow<Pair<String?, String?>> = context.dataStore.data.map { prefs ->
        val username = prefs[USER_KEY]
        val password = prefs[PASS_KEY]
        Pair(username, password)
    }

}