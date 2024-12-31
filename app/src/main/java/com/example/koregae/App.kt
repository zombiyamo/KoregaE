package com.example.koregae

import android.app.Application
import com.example.koregae.di.appModule
import com.example.koregae.ui.viewModel.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, viewModelModule)
        }
    }
}
