package ru.spbstu.trkpo.musicservice.api.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.dto.MyTrack
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack
import se.michaelthelin.spotify.model_objects.specification.Track
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest
import java.io.IOException
import java.net.URI
import kotlin.collections.ArrayList

@Component
class SpotifyServiceApi(
    private final val spotifyServiceApiConfig: SpotifyServiceApiConfig
): MusicServiceApi {

    private var spotifyApi = SpotifyApi.Builder()
        .setClientId(spotifyServiceApiConfig.clientId)
        .setClientSecret(spotifyServiceApiConfig.clientSecret)
        .setRedirectUri(URI.create(spotifyServiceApiConfig.redirectUri))
        .build()

    override fun getUrl(tgBotId: String): String {
        val scope = getReadPlaylistScopes()

        return spotifyServiceApiConfig.baseOAuthUrl +
                "response_type=" + spotifyServiceApiConfig.responseType + "&" +
                "client_id=" + spotifyServiceApiConfig.clientId + "&" +
                "scope=" + scope + "&" +
                "redirect_uri=" + spotifyServiceApiConfig.redirectUri + "&" +
                "state=" + tgBotId
    }

    override fun register(authCode: String): TokensPair? {
        val authorizationCodeRequest = spotifyApi?.authorizationCode(authCode)?.build()
        return try {
            executeAuthCodeRequest(authorizationCodeRequest)
        } catch (e: SpotifyWebApiException) {
            null
        } catch (e: IOException) {
            null
        }
    }

    override fun getTracksFromPlaylist(name: String?, accessToken: String?): ReturnedPlaylist? {
        spotifyApi?.accessToken = accessToken

        return if (name == null || name == "") {
            // get the user's liked songs REQUEST
            val getUsersSavedTracksRequest = spotifyApi?.usersSavedTracks?.build()
            val tracks = executeGetUsersSavedTracksRequest(getUsersSavedTracksRequest)?.map { t ->
                val artists = getArtistsOfTrackInOneString(t.artists)
                MyTrack(t.name, artists, t.album.name)
            }

            ReturnedPlaylist(getSavedTracksPlaylistName(), tracks)
        } else {
            // get the user's playlists REQUEST
            val getListOfCurrentUsersPlaylistsRequest = spotifyApi?.listOfCurrentUsersPlaylists?.build()
            val playlists = executeGetListOfCurrentUsersPlaylistsRequest(getListOfCurrentUsersPlaylistsRequest)
            val nameLowerCase = name.lowercase()
            val foundPlaylist = playlists?.find { p -> p.name.lowercase() == nameLowerCase }
                ?: throw HttpClientErrorException(HttpStatus.NOT_FOUND)

            // get the items from the above playlist REQUEST
            val getPlaylistsItemsRequest = spotifyApi?.getPlaylistsItems(foundPlaylist.id)?.build()
            val playlistTracks = executeGetPlaylistsItemsRequest(getPlaylistsItemsRequest)

            val tracksArray: ArrayList<MyTrack> = ArrayList()
            playlistTracks?.forEach { item ->
                val trackId = item.track?.id
                try {
                    // get the track of the above item REQUEST
                    val getTrackRequest = spotifyApi?.getTrack(trackId)?.build()
                    val track = executeGetTrackRequest(getTrackRequest)
                    val artists = getArtistsOfTrackInOneString(track!!.artists)
                    tracksArray.add(MyTrack(track.name, artists, track.album.name))
                } catch (e: IOException) {
                    println(e.message)
                } catch (e: SpotifyWebApiException) {
                    println(e.message)
                }
            }

            ReturnedPlaylist(getCustomPlaylistName(name), tracksArray)
        }
    }

    override fun refreshTokens(refreshToken: String?): TokensPair? {
        spotifyApi?.refreshToken = refreshToken
        val authorizationCodeRefreshRequest = spotifyApi?.authorizationCodeRefresh()?.build()
        return try {
            executeAuthCodeRefreshRequest(authorizationCodeRefreshRequest)
        } catch (e: SpotifyWebApiException) {
            null
        } catch (e: IOException) {
            null
        }
    }

    override fun getPlaylistsList(accessToken: String?): List<String>? {
        spotifyApi?.accessToken = accessToken
        // get the user's playlists REQUEST
        val getListOfCurrentUsersPlaylistsRequest = spotifyApi?.listOfCurrentUsersPlaylists?.build()
        return executeGetListOfCurrentUsersPlaylistsRequest(getListOfCurrentUsersPlaylistsRequest)
            ?.map { item -> item.name }
    }

    override fun getSavedTracksPlaylistName(): String {
        return "Музыка из Spotify"
    }

    override fun getCustomPlaylistName(name: String?): String {
        return "$name from Spotify"
    }

    override fun getReadPlaylistScopes(): String {
        return "user-library-read%20user-top-read%20playlist-read-private"
    }

    @Throws(Exception::class)
    internal fun executeAuthCodeRequest(authorizationCodeRequest: AuthorizationCodeRequest?): TokensPair {
        val authorizationCodeCredentials = authorizationCodeRequest?.execute()
        return TokensPair(authorizationCodeCredentials?.accessToken, authorizationCodeCredentials?.refreshToken)
    }

    internal fun executeGetUsersSavedTracksRequest(
        getUsersSavedTracksRequest: GetUsersSavedTracksRequest?
    ): List<Track>? {

        val savedTracks = getUsersSavedTracksRequest?.execute()
        return savedTracks?.items?.map {
                t -> t.track
        }
    }

    internal fun executeGetListOfCurrentUsersPlaylistsRequest(
        getListOfCurrentUsersPlaylistsRequest: GetListOfCurrentUsersPlaylistsRequest?
    ): Array<PlaylistSimplified>? {

        return getListOfCurrentUsersPlaylistsRequest?.execute()?.items
    }

    internal fun executeGetPlaylistsItemsRequest(
        getPlaylistsItemsRequest: GetPlaylistsItemsRequest?
    ): Array<PlaylistTrack>? {
        return getPlaylistsItemsRequest?.execute()?.items
    }

    internal fun executeGetTrackRequest(
        getTrackRequest: GetTrackRequest?
    ): Track? {
        return getTrackRequest?.execute()
    }

    @Throws(Exception::class)
    internal fun executeAuthCodeRefreshRequest(
        authorizationCodeRefreshRequest: AuthorizationCodeRefreshRequest?
    ): TokensPair {
        val authorizationCodeCredentials = authorizationCodeRefreshRequest?.execute()
        return TokensPair(authorizationCodeCredentials?.accessToken, authorizationCodeCredentials?.refreshToken)
    }

    private fun getArtistsOfTrackInOneString(artists: Array<ArtistSimplified>): String {
        return artists
            .map { a -> a.name }
            .reduce { acc, s -> "$acc, $s" }
    }
}