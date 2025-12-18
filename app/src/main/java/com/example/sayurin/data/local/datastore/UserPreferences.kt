package com.example.sayurin.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val USER_ID = intPreferencesKey("user_id")
        val ROLE = stringPreferencesKey("role")
        val NAME = stringPreferencesKey("name")
    }

    suspend fun saveUser(userId: Int, role: String, name: String) {
        dataStore.edit { pref ->
            pref[USER_ID] = userId
            pref[ROLE] = role
            pref[NAME] = name
        }
    }

    suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }

    val userId: Flow<Int> = dataStore.data.map { it[USER_ID] ?: 0 }
    val role: Flow<String> = dataStore.data.map { it[ROLE] ?: "" }
    val name: Flow<String> = dataStore.data.map { it[NAME] ?: "" }
}
