package com.example.koregae.data.remote.api

import com.example.koregae.data.remote.model.OAuthConfig
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.oauth.OAuth10aService

class OAuthServiceFactory {
    fun create(config: OAuthConfig, api: HatenaApi): OAuth10aService {
        return ServiceBuilder(config.consumerKey)
            .apiSecret(config.consumerSecret)
            .callback(config.callback)
            .build(api)
    }
}
