package ru.spbstu.trkpo.musicservice.api.impl

import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import java.io.IOException
import java.net.URI
import java.util.*

class SpotifyServiceApi: MusicServiceApi {
    private var baseOAuthUrl: String? = null
    private var responseType: String? = null
    private var clientId: String? = null
    private var clientSecret: String? = null
    private var redirectUri: String? = null

    private var spotifyApi: SpotifyApi? = null

    override fun getUrl(tgBotId: String): String {
        val scope = getReadPlaylistScopes()

        return baseOAuthUrl +
                    "response_type=" + responseType + "&" +
                    "client_id=" + clientId + "&" +
                    "scope=" + scope + "&" +
                    "redirect_uri=" + redirectUri + "&" +
                    "state=" + tgBotId
    }

    /**
     * TODO: раскомментить реальный запрос к Spotify API
     * */
    override fun register(authCode: String): TokensPair? {
        val authorizationCodeRequest = spotifyApi?.authorizationCode(authCode)?.build()
        return try {
//            val authorizationCodeCredentials = authorizationCodeRequest?.execute()
//            TokensPair(authorizationCodeCredentials?.accessToken, authorizationCodeCredentials?.refreshToken)

            TokensPair("test", "test")
        } catch (e: SpotifyWebApiException) {
            null
        } catch (e: IOException) {
            null
        }
    }

    override fun setProperties(properties: Properties) {
        baseOAuthUrl = properties.getProperty("spotify.oauth.base-oauth-url")
        responseType = properties.getProperty("spotify.oauth.response-type")
        clientId = properties.getProperty("spotify.oauth.client-id")
        clientSecret = properties.getProperty("spotify.oauth.client-secret")
        redirectUri = properties.getProperty("spotify.oauth.redirect-uri")

        spotifyApi = SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(URI.create(redirectUri))
            .build()
    }

    override fun getReadPlaylistScopes(): String {
        return "user-library-read%20user-top-read%20playlist-read-private"
    }
}