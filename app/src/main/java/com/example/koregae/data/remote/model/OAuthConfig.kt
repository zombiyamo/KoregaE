package com.example.koregae.data.remote.model

data class OAuthConfig(
    val authorizeUrl: String,
    val callback: String,
    val consumerKey: String,
    val consumerSecret: String,
)
