package com.example.koregae.data.remote.api

import com.github.scribejava.core.model.*
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IOAuthService {
    suspend fun getRequestToken(): OAuth1RequestToken
    suspend fun getAccessToken(requestToken: OAuth1RequestToken, verifier: String): OAuth1AccessToken
    suspend fun fetchUserData(accessToken: OAuth1AccessToken): String
}

open class OAuthService(
    private val oauthService: OAuth10aService
) : IOAuthService {

    override suspend fun getRequestToken(): OAuth1RequestToken = withContext(Dispatchers.IO) {
        oauthService.requestToken
    }

    override suspend fun getAccessToken(
        requestToken: OAuth1RequestToken,
        verifier: String
    ): OAuth1AccessToken = withContext(Dispatchers.IO) {
        oauthService.getAccessToken(requestToken, verifier)
    }

    override suspend fun fetchUserData(accessToken: OAuth1AccessToken): String = withContext(Dispatchers.IO) {
        val url = "https://bookmark.hatenaapis.com/rest/1/my"
        try {
            val oauthRequest = OAuthRequest(Verb.GET, url)
            oauthService.signRequest(accessToken, oauthRequest)
            val response = oauthService.execute(oauthRequest)
            response.body
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
