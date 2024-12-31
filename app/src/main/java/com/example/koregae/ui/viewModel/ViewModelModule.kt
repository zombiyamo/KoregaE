package com.example.koregae.ui.viewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { OAuthViewModel(get(), get()) }
}
