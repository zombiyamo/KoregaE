package com.example.koregae.ui.viewModel

import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        OAuthViewModel(
            oAuthService = get(),
            config = get(),
            ioDispatcher = Dispatchers.IO
        )
    }
}
