package com.example.koregae.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

interface CustomTabsLauncher {
    fun launchUrl(url: String)
}

class CustomTabsLauncherImpl(private val context: Context) : CustomTabsLauncher {
    override fun launchUrl(url: String) {
        if (context is Activity) {
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(context, Uri.parse(url))
        } else {
            throw IllegalStateException("Context is not an Activity")
        }
    }
}