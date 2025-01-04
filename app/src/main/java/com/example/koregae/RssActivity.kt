package com.example.koregae

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.koregae.data.local.UserInfoManager
import com.example.koregae.data.remote.RssRepository
import com.example.koregae.ui.view.RssScreen
import com.example.koregae.ui.viewModel.RssViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class RssActivity : ComponentActivity() {
    private val userInfoManager: UserInfoManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = HttpClient()
        val repository = RssRepository(client)
        val viewModel = RssViewModel(repository)

        lifecycleScope.launch {
            userInfoManager.userInfoFlow
                .collect { userInfo ->
                    val userName = userInfo?.name ?: "sample"
                    val url = "https://b.hatena.ne.jp/$userName/bookmark.rss"
                    viewModel.loadRssFeed(url)
                }
        }

        setContent {
            RssScreen(viewModel)
        }
    }
}
