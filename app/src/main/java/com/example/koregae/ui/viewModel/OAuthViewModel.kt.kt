package com.example.koregae.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koregae.data.remote.api.OAuthService
import com.example.koregae.data.remote.model.OAuthConfig
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.launch

class OAuthViewModel(
    private val oAuthService: OAuthService,
    private val config: OAuthConfig
) : ViewModel() {

    fun startOAuthFlow(onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val requestToken = oAuthService.getRequestToken().token
                val authUrl = "${config.authorizeUrl}?oauth_token=$requestToken"
                onSuccess(authUrl)
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun completeOAuthFlow(
        pinCode: String,
        onSuccess: (OAuth1AccessToken) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val accessToken = oAuthService.getAccessToken(pinCode)
                onSuccess(accessToken)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun fetchUserData(accessToken: OAuth1AccessToken,
                      onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val data = oAuthService.fetchUserData(accessToken)
                onSuccess(data)
            } catch (e: Exception) {
                e.localizedMessage ?: "Unknown error occurred"
            }
        }
    }
}
