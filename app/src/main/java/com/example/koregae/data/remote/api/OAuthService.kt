package com.example.koregae.data.remote.api

import com.example.koregae.data.local.UserInfoManager
import com.example.koregae.data.remote.model.UserInfo
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

interface IOAuthService {
    suspend fun getRequestToken(): OAuth1RequestToken
    suspend fun getAccessToken(verifier: String): OAuth1AccessToken
    suspend fun fetchUserData(accessToken: OAuth1AccessToken): UserInfo
}

open class OAuthService(
    private val oauthService: OAuth10aService,
    private val dataStoreManager: UserInfoManager
) : IOAuthService {
    private lateinit var requestToken: OAuth1RequestToken

    override suspend fun getRequestToken(): OAuth1RequestToken = withContext(Dispatchers.IO) {
        requestToken = oauthService.requestToken
        requestToken
    }

    override suspend fun getAccessToken(
        verifier: String
    ): OAuth1AccessToken = withContext(Dispatchers.IO) {
        oauthService.getAccessToken(requestToken, verifier)
    }

    override suspend fun fetchUserData(accessToken: OAuth1AccessToken): UserInfo =
        withContext(Dispatchers.IO) {
        val url = "https://bookmark.hatenaapis.com/rest/1/my"
        try {
            val oauthRequest = OAuthRequest(Verb.GET, url)
            oauthService.signRequest(accessToken, oauthRequest)
            val response = oauthService.execute(oauthRequest)
            val withUnknownKeys = Json { ignoreUnknownKeys = true }
            val userInfo = withUnknownKeys.decodeFromString<UserInfo>(response.body)

            dataStoreManager.saveUserInfo(userInfo)

            userInfo
        } catch (e: Exception) {
            throw IllegalStateException("Failed to fetch user data: ${e.message}")
        }
    }
}
