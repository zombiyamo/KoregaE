package com.example.koregae.data.remote.api

import com.github.scribejava.core.builder.api.DefaultApi10a
import com.github.scribejava.core.model.OAuth1RequestToken
import java.net.URLEncoder

class HatenaApi(
    private val requestTokenUrl: String = "https://www.hatena.com/oauth/initiate",
    private val accessTokenUrl: String = "https://www.hatena.com/oauth/token",
    private val authorizeUrl: String = "https://www.hatena.ne.jp/touch/oauth/authorize"
) : DefaultApi10a() {

    override fun getRequestTokenEndpoint(): String {
        return "$requestTokenUrl?scope=${URLEncoder.encode("read_public,read_private", "UTF-8")}"
    }

    override fun getAccessTokenEndpoint(): String {
        return accessTokenUrl
    }

    override fun getAuthorizationBaseUrl(): String {
        return authorizeUrl
    }

    override fun getAuthorizationUrl(requestToken: OAuth1RequestToken): String {
        return "$authorizeUrl?oauth_token=${requestToken.token}"
    }
}
