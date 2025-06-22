package com.joseph.salesorderapp.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Access DataStore
    private val dataStore = context.dataStore

    // Define Keys
    companion object {
        val KEY_AUTH_TOKEN = stringPreferencesKey("auth_token")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_IS_DOMAIN_ADDED = booleanPreferencesKey("is_domain_added")
        val KEY_DOMAIN_ADDRESS = stringPreferencesKey("domain_address")
        val KEY_PRINTER_NAME = stringPreferencesKey("print_address")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
    }

    // Save string
    suspend fun saveString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { it[key] = value }
    }

    // Save boolean
    suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { it[key] = value }
    }

    // Get string (nullable)
    fun getString(key: Preferences.Key<String>): Flow<String?> {
        return dataStore.data.map { it[key] }
    }

    // Get string with default
    fun getStringOrDefault(key: Preferences.Key<String>, default: String = ""): Flow<String> {
        return dataStore.data.map { it[key] ?: default }
    }

    // Get boolean
    fun getBoolean(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return dataStore.data.map { it[key] ?: false }
    }

    // Clear all data
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
