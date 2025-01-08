package com.example.koregae

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.koregae.ui.viewModel.FakeOAuthViewModel
import com.example.koregae.ui.viewModel.OAuthUiState
import com.example.koregae.utils.FakeCustomTabsLauncher
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeOAuthViewModel = FakeOAuthViewModel()
    private val fakeCustomTabsLauncher = FakeCustomTabsLauncher()

    init {
        startKoin {
            modules(
                module {
                    single { fakeOAuthViewModel }
                    single { fakeCustomTabsLauncher }
                }
            )
        }
    }

    @Test
    fun test_NavigateToOAuthScreen_WhenStateIsError() {
        // Arrange
        fakeOAuthViewModel.setUiState(OAuthUiState.Error(Throwable()))

        // Act
        composeTestRule.setContent {
            MainActivity()
        }

        // Assert
        composeTestRule.onNodeWithText("OAuth Screen").assertIsDisplayed()
    }

    @Test
    fun test_NavigateToRssScreen_WhenStateIsUserDataLoaded() {
        // Arrange
        fakeOAuthViewModel.setUiState(OAuthUiState.UserDataLoaded("user"))

        // Act
        composeTestRule.setContent {
            MainActivity()
        }

        // Assert
        composeTestRule.onNodeWithText("RSS Screen").assertIsDisplayed()
    }
}
