package com.example.koregae.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.koregae.data.local.FakeOAuthTokenManager
import com.example.koregae.data.local.OAuthTokenManager
import com.example.koregae.data.remote.api.FakeOAuthService
import com.example.koregae.data.remote.model.FakeOAuthConfig
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OAuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var oAuthService: FakeOAuthService
    private lateinit var config: FakeOAuthConfig
    private lateinit var oAuthTokenManager: OAuthTokenManager
    private lateinit var viewModel: OAuthViewModel

    @BeforeEach
    fun setUp() {
        oAuthService = FakeOAuthService()
        config = FakeOAuthConfig()
        oAuthTokenManager = FakeOAuthTokenManager()
        viewModel = OAuthViewModel(oAuthService, config, oAuthTokenManager)
    }

    @Test
    fun `OAuthフローを開始する`() = runTest {
        oAuthService.shouldThrowException = false
        val expectedToken = "fakeRequestToken"
        val expectedAuthUrl = "https://example.com/authorize?oauth_token=$expectedToken"
        config.authorizeUrl = "https://example.com/authorize"


        viewModel.startOAuthFlow()


        val state = viewModel.uiState.first { it is OAuthViewModel.OAuthUiState.OAuthFlowStarted }
        assert(state is OAuthViewModel.OAuthUiState.OAuthFlowStarted)
        val resultUrl = (state as OAuthViewModel.OAuthUiState.OAuthFlowStarted).authUrl
        assertEquals(expectedAuthUrl, resultUrl)
    }

    @Test
    fun `OAuthフローを完了する`() = runTest {
        oAuthService.shouldThrowException = false
        val expectedToken =
            OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret", "fakeRawResponse")


        viewModel.completeOAuthFlow(pinCode = "1234")

        val state = viewModel.uiState.first { it is OAuthViewModel.OAuthUiState.TokenLoaded }
        assert(state is OAuthViewModel.OAuthUiState.TokenLoaded)

        val resultToken = (state as OAuthViewModel.OAuthUiState.TokenLoaded).token
        assertEquals(expectedToken.token, resultToken.token)
        assertEquals(expectedToken.tokenSecret, resultToken.tokenSecret)
        assertEquals(expectedToken.rawResponse, resultToken.rawResponse)
    }

    @Test
    fun `ユーザーデータを取得する`() = runTest {
        oAuthService.shouldThrowException = false
        val expectedUserData = "fake_user_id"
        val accessToken = OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret")


        viewModel.fetchUserData(accessToken)


        val state = viewModel.uiState.first { it is OAuthViewModel.OAuthUiState.UserDataLoaded }
        assert(state is OAuthViewModel.OAuthUiState.UserDataLoaded)
        val resultData = (state as OAuthViewModel.OAuthUiState.UserDataLoaded).userName
        assertEquals(expectedUserData, resultData)
    }

    @Test
    fun `OAuthフローでエラーが発生する`() = runTest {
        oAuthService.shouldThrowException = true


        viewModel.startOAuthFlow()


        val state = viewModel.uiState.first { it is OAuthViewModel.OAuthUiState.Error }
        assert(state is OAuthViewModel.OAuthUiState.Error)
        val error = (state as OAuthViewModel.OAuthUiState.Error).throwable
        assertEquals("Failed to get Request Token", error.message)
    }

    @Test
    fun `ユーザーデータ取得時にエラーが発生する`() = runTest {
        oAuthService.shouldThrowException = true
        val accessToken = OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret")


        viewModel.fetchUserData(accessToken)


        val state = viewModel.uiState.first { it is OAuthViewModel.OAuthUiState.Error }
        assert(state is OAuthViewModel.OAuthUiState.Error)
        val error = (state as OAuthViewModel.OAuthUiState.Error).throwable
        assertEquals("Failed to fetch user data", error.message)
    }
}
