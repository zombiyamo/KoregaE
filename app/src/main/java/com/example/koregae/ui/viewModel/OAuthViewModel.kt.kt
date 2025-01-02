package com.example.koregae.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koregae.data.remote.api.IOAuthService
import com.example.koregae.data.remote.model.OAuthConfig
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OAuthViewModel(
    private val oAuthService: IOAuthService,
    private val config: OAuthConfig,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    fun startOAuthFlow(
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            runCatching {
                val requestToken = oAuthService.getRequestToken().token
                val authUrl = "${config.authorizeUrl}?oauth_token=$requestToken"
                onSuccess(authUrl)
            }.onFailure {
                onError(it)
            }
        }
    }

    fun completeOAuthFlow(
        pinCode: String,
        onSuccess: (OAuth1AccessToken) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            runCatching {
                val requestToken = oAuthService.getRequestToken()
                val accessToken = oAuthService.getAccessToken(requestToken, pinCode)
                onSuccess(accessToken)
            }.onFailure {
                onError(it)
            }
        }
    }

    fun fetchUserData(
        accessToken: OAuth1AccessToken,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            runCatching {
                val data = oAuthService.fetchUserData(accessToken)
                onSuccess(data)
            }.onFailure {
                onError(it)
            }
        }
    }
}
