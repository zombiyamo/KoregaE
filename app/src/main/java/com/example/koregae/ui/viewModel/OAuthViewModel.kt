package com.example.koregae.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koregae.data.local.OAuthTokenManager
import com.example.koregae.data.remote.api.IOAuthService
import com.example.koregae.data.remote.model.OAuthConfig
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OAuthUiState {
    data object Idle : OAuthUiState()
    data object NoToken : OAuthUiState()
    data class TokenLoaded(val token: OAuth1AccessToken) : OAuthUiState()
    data class OAuthFlowStarted(val authUrl: String) : OAuthUiState()
    data class UserDataLoaded(val userName: String) : OAuthUiState()
    data class Error(val throwable: Throwable) : OAuthUiState()
}

interface OAuthViewModel {
    val uiState: StateFlow<OAuthUiState>
    fun loadAccessToken()
    fun startOAuthFlow()
    fun completeOAuthFlow(pinCode: String)
    fun fetchUserData(accessToken: OAuth1AccessToken)
}

class OAuthViewModelImpl(
    private val oAuthService: IOAuthService,
    private val config: OAuthConfig,
    private val tokenManager: OAuthTokenManager
) : ViewModel(), OAuthViewModel {
    private val _uiState = MutableStateFlow<OAuthUiState>(OAuthUiState.Idle)
    override val uiState: StateFlow<OAuthUiState> = _uiState.asStateFlow()

    init {
        loadAccessToken()
    }

    override fun loadAccessToken() {
        if (_uiState.value is OAuthUiState.OAuthFlowStarted) return
        viewModelScope.launch {
            runCatching {
                tokenManager.getAccessToken()
            }.onSuccess { token ->
                if (token != null) {
                    _uiState.value = OAuthUiState.TokenLoaded(token)
                    fetchUserData(token)
                } else {
                    startOAuthFlow()
                }
            }.onFailure {
                _uiState.value = OAuthUiState.Error(it)
            }
        }
    }

    override fun startOAuthFlow() {
        viewModelScope.launch {
            runCatching {
                val requestToken = oAuthService.getRequestToken().token
                "${config.authorizeUrl}?oauth_token=$requestToken"
            }.onSuccess { authUrl ->
                _uiState.value = OAuthUiState.OAuthFlowStarted(authUrl)
            }.onFailure {
                _uiState.value = OAuthUiState.Error(it)
            }
        }
    }

    override fun completeOAuthFlow(pinCode: String) {
        viewModelScope.launch {
            runCatching {
                val accessToken = oAuthService.getAccessToken(pinCode)
                tokenManager.saveAccessToken(accessToken)
                accessToken
            }.onSuccess { token ->
                _uiState.value = OAuthUiState.TokenLoaded(token)
                fetchUserData(token)
            }.onFailure {
                _uiState.value = OAuthUiState.Error(it)
            }
        }
    }

    override fun fetchUserData(accessToken: OAuth1AccessToken) {
        viewModelScope.launch {
            runCatching {
                oAuthService.fetchUserData(accessToken).name
            }.onSuccess { name ->
                _uiState.value = OAuthUiState.UserDataLoaded(name)
            }.onFailure {
                _uiState.value = OAuthUiState.Error(it)
            }
        }
    }
}
