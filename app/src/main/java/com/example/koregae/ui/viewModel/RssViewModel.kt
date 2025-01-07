package com.example.koregae.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koregae.data.local.UserInfoManager
import com.example.koregae.data.remote.RssRepository
import com.example.koregae.data.remote.model.RssItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class RssViewModel(
    private val repository: RssRepository,
    private val userInfoManager: UserInfoManager
) : ViewModel() {
    private val _rssItems = MutableStateFlow<List<RssItem>>(emptyList())
    val rssItems: StateFlow<List<RssItem>> get() = _rssItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        loadRssFeed()
    }

    private fun loadRssFeed() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userInfoManager.userInfoFlow.take(1).collect { userInfo ->
                    val userName = userInfo?.name ?: "sample"
                    val url = "https://b.hatena.ne.jp/$userName/bookmark.rss"
                    _rssItems.value = repository.fetchRssFeed(url)
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
