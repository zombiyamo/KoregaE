package com.example.koregae.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.koregae.data.remote.api.FakeOAuthService
import com.example.koregae.data.remote.model.FakeOAuthConfig
import com.github.scribejava.core.model.OAuth1AccessToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail

@ExperimentalCoroutinesApi
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OAuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var config: FakeOAuthConfig
    private lateinit var oAuthService: FakeOAuthService
    private lateinit var viewModel: OAuthViewModel

    @BeforeEach
    fun setUp() {
        config = FakeOAuthConfig()
        oAuthService = FakeOAuthService()
        viewModel = OAuthViewModel(oAuthService, config)
    }

    @Test
    fun `OAuthフローを開始する`() = runTest {
        val expectedToken = "fakeRequestToken"
        val expectedAuthUrl = "https://example.com/authorize?oauth_token=$expectedToken"

        config.authorizeUrl = "https://example.com/authorize"

        viewModel.startOAuthFlow(
            onSuccess = { resultUrl -> assertEquals(expectedAuthUrl, resultUrl) },
            onError = { fail("エラーが発生しました: ${it.message}") }
        )
    }

    @Test
    fun `OAuthフローを完了する`() = runTest {
        val expectedToken = OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret")

        viewModel.completeOAuthFlow(
            pinCode = "1234",
            onSuccess = { resultToken ->
                assertEquals(expectedToken.token, resultToken.token)
                assertEquals(expectedToken.tokenSecret, resultToken.tokenSecret)
            },
            onError = { fail("エラーが発生しました: ${it.message}") }
        )
    }

    @Test
    fun `ユーザーデータを取得する`() = runTest {
        val expectedUserData = """
            {
                "id": "fake_user_id",
                "name": "Fake User",
                "email": "fakeuser@example.com"
            }
        """.trimIndent()

        val accessToken = OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret")

        viewModel.fetchUserData(
            accessToken = accessToken,
            onSuccess = { resultData -> assertEquals(expectedUserData, resultData) },
            onError = { fail("エラーが発生しました: ${it.message}") }
        )
    }

    @Test
    fun `OAuthフローでエラーが発生する`() = runTest {
        oAuthService.shouldThrowException = true

        viewModel.startOAuthFlow(
            onSuccess = { fail("成功してはいけません") },
            onError = { error -> assertEquals("Failed to get Request Token", error.message) }
        )
    }

    @Test
    fun `ユーザーデータ取得時にエラーが発生する`() = runTest {
        oAuthService.shouldThrowException = true

        val accessToken = OAuth1AccessToken("fakeAccessToken", "fakeAccessTokenSecret")

        viewModel.fetchUserData(
            accessToken = accessToken,
            onSuccess = { fail("成功してはいけません") },
            onError = { error -> assertEquals("Failed to fetch user data", error.message) }
        )
    }
}
