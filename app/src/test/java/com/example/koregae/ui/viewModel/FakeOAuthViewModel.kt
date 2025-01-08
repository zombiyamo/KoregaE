package com.example.koregae.ui.viewModel

import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeOAuthViewModel : OAuthViewModel {
    private val _uiState = MutableStateFlow<OAuthUiState>(OAuthUiState.NoToken)
    override val uiState: StateFlow<OAuthUiState> = _uiState
    override fun loadAccessToken() {
        TODO("Not yet implemented")
    }

    override fun startOAuthFlow() {
        TODO("Not yet implemented")
    }

    override fun completeOAuthFlow(pinCode: String) {
        TODO("Not yet implemented")
    }

    override fun fetchUserData(accessToken: OAuth1AccessToken) {
        TODO("Not yet implemented")
    }

    fun setUiState(state: OAuthUiState) {
        _uiState.value = state
    }
}