package com.example.koregae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koregae.ui.view.OAuthScreen
import com.example.koregae.ui.view.RssScreen
import com.example.koregae.ui.viewModel.OAuthViewModel
import com.example.koregae.ui.viewModel.OAuthViewModel.OAuthUiState
import com.example.koregae.utils.CustomTabsLauncher
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val oauthViewModel: OAuthViewModel by viewModel()
    private val customTabsLauncher: CustomTabsLauncher by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "oauth") {
                composable("oauth") {
                    OAuthScreen(customTabsLauncher = customTabsLauncher)
                }
                composable("rss") {
                    RssScreen(customTabsLauncher = customTabsLauncher)
                }
            }

            // uiState を購読して状態を監視
            val uiState by oauthViewModel.uiState.collectAsState()

            when (uiState) {
                is OAuthUiState.Error, is OAuthUiState.NoToken, is OAuthUiState.OAuthFlowStarted -> {
                    navController.navigate("oauth")
                }

                is OAuthUiState.UserDataLoaded -> {
                    navController.navigate("rss")
                }

                else -> {}
            }
        }
    }
}
