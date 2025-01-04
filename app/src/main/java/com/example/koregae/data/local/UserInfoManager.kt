package com.example.koregae.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.koregae.data.remote.model.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class UserInfoManager(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val USER_INFO_KEY = stringPreferencesKey("user_info")
    }

    suspend fun saveUserInfo(userInfo: UserInfo) {
        dataStore.edit { preferences ->
            preferences[USER_INFO_KEY] = Json.encodeToString(UserInfo.serializer(), userInfo)
        }
    }

    val userInfoFlow: Flow<UserInfo?> = dataStore.data
        .map { preferences ->
            preferences[USER_INFO_KEY]?.let { Json.decodeFromString<UserInfo>(it) }
        }
}