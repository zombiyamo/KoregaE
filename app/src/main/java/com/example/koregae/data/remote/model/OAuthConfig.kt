package com.example.koregae.data.remote.model

open class OAuthConfig(
    open val authorizeUrl: String,
    val callback: String,
    val consumerKey: String,
    val consumerSecret: String,
)
