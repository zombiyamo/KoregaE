package com.example.koregae.di

import android.app.Activity
import com.example.koregae.utils.CustomTabsLauncher
import com.example.koregae.utils.CustomTabsLauncherImpl
import org.koin.dsl.module

val utilsModule = module {
    factory<CustomTabsLauncher> { (activity: Activity) -> CustomTabsLauncherImpl(activity) }
}