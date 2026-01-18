package com.example.sayurin.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_ROLE = stringPreferencesKey("user_role")
    }

    /**
     * Menyimpan data sesi setelah login berhasil
     */
    suspend fun saveSession(userId: Int, name: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId
            prefs[USER_NAME] = name
            prefs[USER_ROLE] = role
        }
    }

    /**
     * Mengambil ID User secara Real-time (Flow)
     */
    val userId: Flow<Int?> = context.dataStore.data.map { it[USER_ID] }

    /**
     * Mengambil Nama User secara Real-time (Flow)
     * Sangat berguna untuk dikirim ke Admin saat melakukan Chat
     */
    val userName: Flow<String?> = context.dataStore.data.map { it[USER_NAME] }

    /**
     * Mengambil Role User secara Real-time (Flow)
     */
    val userRole: Flow<String?> = context.dataStore.data.map { it[USER_ROLE] }

    /**
     * Fungsi Helper untuk mengambil UserId secara langsung di dalam Coroutine
     */
    suspend fun getUserIdSync(): Int {
        return context.dataStore.data.map { it[USER_ID] ?: 0 }.first()
    }

    /**
     * Fungsi Helper untuk mengambil Nama secara langsung di dalam Coroutine
     */
    suspend fun getUserNameSync(): String {
        return context.dataStore.data.map { it[USER_NAME] ?: "" }.first()
    }

    /**
     * Menghapus semua data sesi (saat logout)
     */
    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}