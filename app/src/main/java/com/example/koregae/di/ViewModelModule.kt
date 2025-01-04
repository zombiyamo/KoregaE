package com.example.koregae.di

import com.example.koregae.ui.viewModel.OAuthViewModel
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
}
