package com.example.koregae.di

import com.example.koregae.BuildConfig
import com.example.koregae.data.remote.api.OAuthService
import com.example.koregae.data.remote.model.OAuthConfig
import org.koin.dsl.module

val NetworkModule = module {
    single {
        OAuthConfig(
            authorizeUrl = "https://www.hatena.ne.jp/touch/oauth/authorize",
            callback = "oob",
            consumerKey = BuildConfig.CONSUMER_KEY,
            consumerSecret = BuildConfig.CONSUMER_SECRET,
        )
    }
    single { OAuthService(get()) }
}
