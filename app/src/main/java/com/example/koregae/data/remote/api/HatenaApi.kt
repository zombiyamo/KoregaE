package com.example.koregae.data.remote.api

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1RequestToken
import com.example.koregae.data.remote.model.OAuthConfig
import com.github.scribejava.core.builder.api.DefaultApi10a
import com.github.scribejava.core.oauth.OAuth10aService
import java.net.URLEncoder

class HatenaApi private constructor() : DefaultApi10a() {
    private val requestTokenUrl = "https://www.hatena.com/oauth/initiate"
    private val accessTokenUrl = "https://www.hatena.com/oauth/token"
    private val authorizeUrl = "https://www.hatena.ne.jp/touch/oauth/authorize"

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

    companion object {
        fun instance(oAuthConfig: OAuthConfig): OAuth10aService {
            return ServiceBuilder(oAuthConfig.consumerKey)
                .apiSecret(oAuthConfig.consumerSecret)
                .callback(oAuthConfig.callback)
                .build(HatenaApi())
        }
    }
}
