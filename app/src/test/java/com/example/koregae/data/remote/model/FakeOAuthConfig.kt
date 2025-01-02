package com.example.koregae.data.remote.model

class FakeOAuthConfig : OAuthConfig(
    authorizeUrl = "https://www.hatena.ne.jp/touch/oauth/authorize",
    callback = "oob",
    consumerKey = "hatenaKey",
    consumerSecret = "hatenaSecret",
) {
    override var authorizeUrl: String = ""
}