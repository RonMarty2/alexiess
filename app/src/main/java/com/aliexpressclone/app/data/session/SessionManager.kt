package com.aliexpressclone.app.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {
    private val userIdKey = longPreferencesKey("logged_in_user_id")

    val currentUserId: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[userIdKey]?.takeIf { it != 0L }
    }

    suspend fun login(userId: Long) {
        context.dataStore.edit { prefs -> prefs[userIdKey] = userId }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs -> prefs.remove(userIdKey) }
    }
}
