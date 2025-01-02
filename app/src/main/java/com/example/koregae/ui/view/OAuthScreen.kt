package com.example.koregae.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.koregae.ui.viewModel.OAuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OAuthScreen(oauthViewModel: OAuthViewModel) {
    val context = LocalContext.current

    var authUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var authSuccessMessage by remember { mutableStateOf("") }

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
            // OAuth開始ボタン
            Button(
                onClick = {
                    oauthViewModel.startOAuthFlow(
                        onSuccess = { url ->
                            authUrl = url
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        onError = { error ->
                            errorMessage = error.message ?: "Unknown error"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start OAuth Flow")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 認証URL表示
            if (authUrl.isNotEmpty()) {
                Text("Auth URL: $authUrl", style = MaterialTheme.typography.bodyLarge)
            }

            // エラーメッセージ表示
            if (errorMessage.isNotEmpty()) {
                Text(
                    "Error: $errorMessage",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // PINコード入力フィールド
            if (authUrl.isNotEmpty()) {
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
                        oauthViewModel.completeOAuthFlow(
                            pinCode,
                            onSuccess = { accessToken ->
                                oauthViewModel.fetchUserData(accessToken, onSuccess = { data ->
                                    Toast.makeText(context, data, Toast.LENGTH_LONG).show()
                                }, onError = { error ->
                                    errorMessage = error.message ?: "Failed to fetch user data"
                                })
                                authSuccessMessage = "Authentication Successful!"
                            },
                            onError = { error ->
                                errorMessage = error.message ?: "Failed to authenticate"
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = pinCode.isNotEmpty()
                ) {
                    Text("Submit PIN Code")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 成功メッセージ表示
            if (authSuccessMessage.isNotEmpty()) {
                Text(
                    authSuccessMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
