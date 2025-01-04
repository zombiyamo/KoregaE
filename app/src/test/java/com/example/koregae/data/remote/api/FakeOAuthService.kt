package com.example.koregae.data.remote.api

import com.example.koregae.data.remote.model.UserInfo
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken

class FakeOAuthService : IOAuthService {

    var shouldThrowException: Boolean = false

    override suspend fun getRequestToken(): OAuth1RequestToken {
        if (shouldThrowException) {
            throw Exception("Failed to get Request Token")
        }
        return OAuth1RequestToken("fakeRequestToken", "fakeRequestTokenSecret")
    }

    override suspend fun getAccessToken(verifier: String): OAuth1AccessToken {
        if (shouldThrowException) {
            throw Exception("Failed to get Access Token")
        }
        return OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret", "fakeRawResponse")
    }

    override suspend fun fetchUserData(accessToken: OAuth1AccessToken): UserInfo {
        if (shouldThrowException) {
            throw Exception("Failed to fetch user data")
        }
        return UserInfo("fake_user_id")
    }
}
