package ru.spbstu.trkpo.musicservice.api.impl

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "spotify.oauth")
class SpotifyServiceApiConfig {
    lateinit var baseOAuthUrl: String
    lateinit var responseType: String
    lateinit var clientId: String
    lateinit var clientSecret: String
    lateinit var redirectUri: String
}
