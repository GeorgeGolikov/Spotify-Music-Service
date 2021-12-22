package ru.spbstu.trkpo.musicservice.api.impl

import org.apache.hc.core5.http.ParseException
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.dto.Track
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified
import java.io.IOException
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class SpotifyServiceApi : MusicServiceApi {
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

    override fun getTracksFromPlaylist(name: String?, accessToken: String?): ReturnedPlaylist? {
        spotifyApi?.accessToken = accessToken

        return if (name == null || name == "") {
            val getUsersSavedTracksRequest = spotifyApi?.usersSavedTracks?.build()

            val savedTracks = getUsersSavedTracksRequest?.execute() // get the user's liked songs REQUEST
            val tracks = savedTracks?.items?.map { t ->
                val track = t.track
                val artists = getArtistsOfTrackInOneString(track.artists)
                Track(track.name, artists, track.album.name)
            }

            ReturnedPlaylist(getSavedTracksPlaylistName(), tracks)
        } else {
            val getListOfCurrentUsersPlaylistsRequest = spotifyApi?.listOfCurrentUsersPlaylists?.build()

            val playlists = getListOfCurrentUsersPlaylistsRequest?.execute() // get the user's playlists REQUEST
            val foundPlaylist = playlists?.items?.find { p -> p.name == name }
                ?: throw HttpClientErrorException(HttpStatus.NOT_FOUND)

            val getPlaylistsItemsRequest = spotifyApi?.getPlaylistsItems(foundPlaylist.id)?.build()
            val playlistTracks = getPlaylistsItemsRequest?.execute() // get the items from the above playlist REQUEST

            val tracksArray: ArrayList<Track> = ArrayList()
            playlistTracks?.items?.forEach { item ->
                val trackId = item?.track?.id
                val getTrackRequest = spotifyApi?.getTrack(trackId)?.build()
                try {
                    val track = getTrackRequest?.execute() // get the track of the above item REQUEST
                    val artists = getArtistsOfTrackInOneString(track!!.artists)
                    tracksArray.add(Track(track.name, artists, track.album.name))
                } catch (e: IOException) {
                    println(e.message)
                } catch (e: SpotifyWebApiException) {
                    println(e.message)
                }
            }

            ReturnedPlaylist(getCustomPlaylistName(name), tracksArray)
        }
    }

    /**
     * TODO: раскомментить реальный запрос к Spotify API
     * */
    override fun refreshTokens(refreshToken: String?): TokensPair? {
        spotifyApi?.refreshToken = refreshToken
        val authorizationCodeRefreshRequest = spotifyApi?.authorizationCodeRefresh()?.build()

        return try {
//            val authorizationCodeCredentials = authorizationCodeRefreshRequest?.execute()
//            TokensPair(authorizationCodeCredentials?.accessToken, authorizationCodeCredentials?.refreshToken)

            TokensPair("test", "test")
        } catch (e: SpotifyWebApiException) {
            null
        } catch (e: IOException) {
            null
        }
    }

    override fun getSavedTracksPlaylistName(): String {
        return "Музыка из Spotify"
    }

    override fun getCustomPlaylistName(name: String?): String {
        return "$name from Spotify"
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

    private fun getArtistsOfTrackInOneString(artists: Array<ArtistSimplified>): String {
        return artists
            .map { a -> a.name }
            .reduce { acc, s -> "$acc, $s" }
    }
}