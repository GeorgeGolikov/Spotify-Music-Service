package ru.spbstu.trkpo.musicservice.api.impl

import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import java.util.*

class SpotifyApi: MusicServiceApi {
    private var baseOAuthUrl: String? = null
    private var responseType: String? = null
    private var clientId: String? = null
    private var clientSecret: String? = null
    private var redirectUri: String? = null

    override fun getUrl(tgBotId: String): String {
        val scope = getReadPlaylistScopes()

        return baseOAuthUrl +
                    "response_type=" + responseType + "&" +
                    "client_id=" + clientId + "&" +
                    "scope=" + scope + "&" +
                    "redirect_uri=" + redirectUri + "&" +
                    "state=" + tgBotId
    }

    override fun setProperties(properties: Properties) {
        baseOAuthUrl = properties.getProperty("spotify.oauth.base-oauth-url")
        responseType = properties.getProperty("spotify.oauth.response-type")
        clientId = properties.getProperty("spotify.oauth.client-id")
        clientSecret = properties.getProperty("spotify.oauth.client-secret")
        redirectUri = properties.getProperty("spotify.oauth.redirect-uri")
    }

    override fun getReadPlaylistScopes(): String {
        return "user-library-read%20user-top-read%20playlist-read-private"
    }
}