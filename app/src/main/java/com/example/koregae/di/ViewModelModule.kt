package com.example.koregae.di

import com.example.koregae.data.remote.RssRepository
import com.example.koregae.ui.viewModel.OAuthViewModel
import com.example.koregae.ui.viewModel.RssViewModel
import io.ktor.client.HttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        OAuthViewModel(
            oAuthService = get(),
            config = get(),
            tokenManager = get()
        )
    }

    single { HttpClient() }

    single { RssRepository(get()) }

    viewModel {
        RssViewModel(
            repository = get(),
            userInfoManager = get()
        )
    }
}
