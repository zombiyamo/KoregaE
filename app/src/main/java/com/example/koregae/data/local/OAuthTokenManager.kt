package com.example.koregae.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.flow.first

open class OAuthTokenManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("oauth_token")
        val TOKEN_SECRET_KEY = stringPreferencesKey("oauth_token_secret")
        val RAW_RESPONSE_KEY = stringPreferencesKey("oauth_raw_response")
    }

    open suspend fun saveAccessToken(accessToken: OAuth1AccessToken) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = accessToken.token
            preferences[TOKEN_SECRET_KEY] = accessToken.tokenSecret
            preferences[RAW_RESPONSE_KEY] = accessToken.rawResponse
        }
    }

    open suspend fun getAccessToken(): OAuth1AccessToken? {
        val preferences = dataStore.data.first()
        val token = preferences[TOKEN_KEY]
        val tokenSecret = preferences[TOKEN_SECRET_KEY]
        val rawResponse = preferences[RAW_RESPONSE_KEY]

        return if (token != null && tokenSecret != null && rawResponse != null) {
            OAuth1AccessToken(token, tokenSecret, rawResponse)
        } else {
            null
        }
    }
}
