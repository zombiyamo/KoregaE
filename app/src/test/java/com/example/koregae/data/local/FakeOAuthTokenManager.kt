package com.example.koregae.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.flow.Flow

class FakeOAuthTokenManager(dataStore: FakeDataStore = FakeDataStore()) :
    OAuthTokenManager(dataStore) {

    override suspend fun saveAccessToken(accessToken: OAuth1AccessToken) {
        // モックのデータを保存
    }

    override suspend fun getAccessToken(): OAuth1AccessToken? {
        return OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret", "fakeRawResponse")
    }
}


class FakeDataStore : DataStore<Preferences> {
    override val data: Flow<Preferences>
        get() = TODO("Not yet implemented")

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        TODO("Not yet implemented")
    }
}
