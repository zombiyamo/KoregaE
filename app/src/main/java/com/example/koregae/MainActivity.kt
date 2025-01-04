package com.example.koregae

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.koregae.ui.view.OAuthScreen
import com.example.koregae.ui.viewModel.OAuthViewModel
import com.example.koregae.ui.viewModel.OAuthViewModel.OAuthUiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val oauthViewModel: OAuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // uiState を購読して状態を監視
            val uiState by oauthViewModel.uiState.collectAsState()

            when (uiState) {
                is OAuthUiState.Loading -> {
                    // 読み込み中のUI
                    // LoadingScreen()
                }

                is OAuthUiState.Error -> {
                    OAuthScreen(oauthViewModel)
                }

                is OAuthUiState.NoToken -> {
                    // トークンがない場合はOAuthフローを開始
                    oauthViewModel.startOAuthFlow()
                    OAuthScreen(oauthViewModel)
                }

                is OAuthUiState.OAuthFlowStarted -> {
                    val authUrl = (uiState as OAuthUiState.OAuthFlowStarted).authUrl
                    OAuthScreen(oauthViewModel, authUrl)
                }

                is OAuthUiState.TokenLoaded -> {
                    val token = (uiState as OAuthUiState.TokenLoaded).token
                    // ユーザーデータの取得
                    oauthViewModel.fetchUserData(token)
                }

                is OAuthUiState.UserDataLoaded -> {
                    val userName = (uiState as OAuthUiState.UserDataLoaded).userName
                    // ユーザー名が取得できたら次の画面へ遷移
                    LaunchedEffect(userName) {
                        val intent = Intent(this@MainActivity, RssActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                OAuthUiState.Idle -> TODO()
            }
        }
    }

    // アクティビティ起動時にアクセストークンをロードする
    override fun onStart() {
        super.onStart()
        oauthViewModel.loadAccessToken()
    }
}
