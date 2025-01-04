package com.example.koregae.ui.view

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.koregae.ui.viewModel.OAuthViewModel
import com.example.koregae.ui.viewModel.OAuthViewModel.OAuthUiState
import com.example.koregae.utils.CustomTabsLauncher
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OAuthScreen(
    oauthViewModel: OAuthViewModel = koinViewModel(),
    customTabsLauncher: CustomTabsLauncher = get()
) {
    val context = LocalContext.current
    val uiState by oauthViewModel.uiState.collectAsState()

    var pinCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("OAuth 1.0a Auth") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (uiState) {
                is OAuthUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is OAuthUiState.Error -> {
                    // エラーメッセージ表示
                    errorMessage =
                        (uiState as OAuthUiState.Error).throwable.message ?: "Unknown error"
                    Text(
                        "Error: $errorMessage",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is OAuthUiState.NoToken -> {
                }

                is OAuthUiState.OAuthFlowStarted -> {
                    // 認証URLを表示
                    val authUrl = (uiState as OAuthUiState.OAuthFlowStarted).authUrl
                    Text("Auth URL: $authUrl", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(16.dp))

                    // URLをブラウザで開くボタン
                    Button(
                        onClick = {
                            customTabsLauncher.launchUrl(authUrl)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open in Browser")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = { pinCode = it },
                        label = { Text("Enter PIN Code") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // PINコード送信ボタン
                    Button(
                        onClick = {
                            oauthViewModel.completeOAuthFlow(pinCode)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = pinCode.isNotEmpty()
                    ) {
                        Text("Submit PIN Code")
                    }
                }

                is OAuthUiState.TokenLoaded -> {
                }

                is OAuthUiState.UserDataLoaded -> {
                    val userName = (uiState as OAuthUiState.UserDataLoaded).userName
                    Toast.makeText(context, "Welcome, $userName!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
