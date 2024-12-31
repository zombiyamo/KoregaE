package com.example.koregae.data.remote.api

import com.example.koregae.data.remote.model.OAuthConfig
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OAuthService(oauthConfig: OAuthConfig) {
    private var oauthService: OAuth10aService = HatenaApi.instance(oauthConfig)
    private lateinit var requestToken: OAuth1RequestToken

    suspend fun getRequestToken(): OAuth1RequestToken = withContext(Dispatchers.IO) {
        requestToken = oauthService.requestToken
        requestToken
    }

    suspend fun getAccessToken(verifier: String): OAuth1AccessToken = withContext(Dispatchers.IO) {
        oauthService.getAccessToken(requestToken, verifier)
    }

    suspend fun fetchUserData(accessToken: OAuth1AccessToken): String {
        val url = "https://bookmark.hatenaapis.com/rest/1/my"

        return withContext(Dispatchers.IO) {
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
}
