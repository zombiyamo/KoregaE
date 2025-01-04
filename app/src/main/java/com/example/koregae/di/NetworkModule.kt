package com.example.koregae.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.koregae.BuildConfig
import com.example.koregae.data.local.OAuthTokenManager
import com.example.koregae.data.local.UserInfoManager
import com.example.koregae.data.remote.api.HatenaApi
import com.example.koregae.data.remote.api.IOAuthService
import com.example.koregae.data.remote.api.OAuthService
import com.example.koregae.data.remote.api.OAuthServiceFactory
import com.example.koregae.data.remote.model.OAuthConfig
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "koregae_datastore")

val networkModule = module {
    single {
        OAuthConfig(
            authorizeUrl = "https://www.hatena.ne.jp/touch/oauth/authorize",
            callback = "oob",
            consumerKey = BuildConfig.CONSUMER_KEY,
            consumerSecret = BuildConfig.CONSUMER_SECRET,
        )
    }

    single { HatenaApi() }

    factory { OAuthServiceFactory() }

    single {
        val config: OAuthConfig = get()
        val api: HatenaApi = get()
        get<OAuthServiceFactory>().create(config, api)
    }

    factory<IOAuthService> {
        OAuthService(get(), get())
    }

    single { UserInfoManager(get()) }

    single { OAuthTokenManager(get()) }

    single<DataStore<Preferences>> {
        get<Context>().dataStore
    }
}
