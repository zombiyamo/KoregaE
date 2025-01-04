package com.example.koregae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koregae.data.local.UserInfoManager
import com.example.koregae.ui.view.OAuthScreen
import com.example.koregae.ui.view.RssScreen
import com.example.koregae.ui.viewModel.OAuthViewModel
import com.example.koregae.ui.viewModel.OAuthViewModel.OAuthUiState
import com.example.koregae.ui.viewModel.RssViewModel
import com.example.koregae.utils.CustomTabsLauncher
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
    private val oauthViewModel: OAuthViewModel by viewModel()
    private val rssViewModel: RssViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val customTabsLauncher: CustomTabsLauncher by inject { parametersOf(this) }

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "rss") {
                composable("rss") {
                    RssScreen(rssViewModel, customTabsLauncher)
                }
            }

            // uiState を購読して状態を監視
            val uiState by oauthViewModel.uiState.collectAsState()

            when (uiState) {
                is OAuthUiState.Loading -> {
                    // 読み込み中のUI
                    // LoadingScreen()
                }

                is OAuthUiState.Error -> {
                    OAuthScreen(customTabsLauncher = customTabsLauncher)
                }

                is OAuthUiState.NoToken -> {
                    OAuthScreen(customTabsLauncher = customTabsLauncher)
                }

                is OAuthUiState.OAuthFlowStarted -> {
                    OAuthScreen(customTabsLauncher = customTabsLauncher)
                }

                is OAuthUiState.TokenLoaded -> {
                    val token = (uiState as OAuthUiState.TokenLoaded).token
                    oauthViewModel.fetchUserData(token)
                }

                is OAuthUiState.UserDataLoaded -> {
                    val userInfoManager: UserInfoManager by inject()
                    rssViewModel.loadRssFeed(userInfoManager)
                    navController.navigate("rss")
                }
            }
        }
    }

    // アクティビティ起動時にアクセストークンをロードする
    override fun onStart() {
        super.onStart()
        val oauthViewModel: OAuthViewModel by viewModel()
        oauthViewModel.loadAccessToken()
    }
}
