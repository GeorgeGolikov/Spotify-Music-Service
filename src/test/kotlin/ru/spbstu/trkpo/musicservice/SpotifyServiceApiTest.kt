package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.HttpClientErrorException
import ru.spbstu.trkpo.musicservice.api.impl.SpotifyServiceApi
import ru.spbstu.trkpo.musicservice.api.impl.SpotifyServiceApiConfig
import ru.spbstu.trkpo.musicservice.dto.MyTrack
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.*
import java.io.IOException

@ExtendWith(MockitoExtension::class)
class SpotifyServiceApiTest {
    @Spy
    private val spotifyServiceApi = SpotifyServiceApi(
        SpotifyServiceApiConfig().apply {
            baseOAuthUrl = BASE_OAUTH_URL
            responseType = RESPONSE_TYPE
            clientId = CLIENT_ID
            clientSecret = CLIENT_SECRET
            redirectUri = REDIRECT_URL
        }
    )

    @Test
    internal fun getUrlTest() {
        val correctUrlString = BASE_OAUTH_URL +
                "response_type=" + RESPONSE_TYPE + "&" +
                "client_id=" + CLIENT_ID + "&" +
                "scope=" + SCOPE + "&" +
                "redirect_uri=" + REDIRECT_URL + "&" +
                "state=" + STATE
        val urlString = spotifyServiceApi.getUrl(STATE)
        assertEquals(urlString, correctUrlString)
    }

    @Test
    internal fun registerTest() {
        `when`(spotifyServiceApi.executeAuthCodeRequest(any()))
            .thenReturn(TokensPair(ACCESS_TOKEN, REFRESH_TOKEN))
        val actualTokensPair = spotifyServiceApi.register(AUTH_CODE)
        assertEquals(ACCESS_TOKEN, actualTokensPair?.accessToken)
        assertEquals(REFRESH_TOKEN, actualTokensPair?.refreshToken)
    }

    @Test
    internal fun registerWhenApiExceptionThrownTest() {
        `when`(spotifyServiceApi.executeAuthCodeRequest(any()))
            .thenThrow(SpotifyWebApiException::class.java)
        val actualTokensPair = spotifyServiceApi.register(AUTH_CODE)
        assertEquals(null, actualTokensPair)
    }

    @Test
    internal fun registerWhenIOExceptionThrownTest() {
        `when`(spotifyServiceApi.executeAuthCodeRequest(any()))
            .thenThrow(IOException::class.java)
        val actualTokensPair = spotifyServiceApi.register(AUTH_CODE)
        assertEquals(null, actualTokensPair)
    }

    @Test
    internal fun getTracksFromPlaylistWhenNameNullTest() {
        val expectedReturnedPlaylist = ReturnedPlaylist(PLAYLIST_NAME, TRACKS)
        `when`(spotifyServiceApi.executeGetUsersSavedTracksRequest(any()))
            .thenReturn(SPOTIFY_TRACKS)
        val actualReturnedPlaylist = spotifyServiceApi.getTracksFromPlaylist("", ACCESS_TOKEN)
        assertEquals(expectedReturnedPlaylist, actualReturnedPlaylist)
    }

    @Test
    internal fun getTracksFromPlaylistWhenNameNotNullTest() {
        val playlistToFind = "Playlist1"
        val playlistsSimplified = arrayOf(
            PlaylistSimplified.Builder().setName(playlistToFind).setId("1").build(),
            PlaylistSimplified.Builder().setName("Playlist2").setId("2").build(),
            PlaylistSimplified.Builder().setName("Playlist3").setId("3").build()
        )
        val playlistTracks = arrayOf(
            PlaylistTrack.Builder().setTrack(SPOTIFY_TRACKS[0]).build(),
            PlaylistTrack.Builder().setTrack(SPOTIFY_TRACKS[1]).build(),
            PlaylistTrack.Builder().setTrack(SPOTIFY_TRACKS[2]).build()
        )
        val playlistReturnedName = "Playlist1 from Spotify"
        val expectedReturnedPlaylist = ReturnedPlaylist(playlistReturnedName, TRACKS)

        `when`(spotifyServiceApi.executeGetListOfCurrentUsersPlaylistsRequest(any()))
            .thenReturn(playlistsSimplified)
        `when`(spotifyServiceApi.executeGetPlaylistsItemsRequest(any()))
            .thenReturn(playlistTracks)
        `when`(spotifyServiceApi.executeGetTrackRequest(any()))
            .thenReturn(SPOTIFY_TRACKS[0])
            .thenReturn(SPOTIFY_TRACKS[1])
            .thenReturn(SPOTIFY_TRACKS[2])

        val actualReturnedPlaylist = spotifyServiceApi.getTracksFromPlaylist(playlistToFind, ACCESS_TOKEN)
        assertEquals(expectedReturnedPlaylist, actualReturnedPlaylist)
    }

    @Test
    internal fun getTracksFromPlaylistWhenPlaylistEmptyTest() {
        val playlistToFind = "Playlist1"
        val playlistsSimplified = arrayOf(
            PlaylistSimplified.Builder().setName("Playlist1").setId("1").build()
        )
        val playlistTracks = emptyArray<PlaylistTrack>()
        val playlistReturnedName = "Playlist1 from Spotify"
        val expectedReturnedPlaylist = ReturnedPlaylist(playlistReturnedName, emptyList())

        `when`(spotifyServiceApi.executeGetListOfCurrentUsersPlaylistsRequest(any()))
            .thenReturn(playlistsSimplified)
        `when`(spotifyServiceApi.executeGetPlaylistsItemsRequest(any()))
            .thenReturn(playlistTracks)

        val actualReturnedPlaylist = spotifyServiceApi.getTracksFromPlaylist(playlistToFind, ACCESS_TOKEN)
        assertEquals(expectedReturnedPlaylist, actualReturnedPlaylist)
    }

    @Test
    internal fun getTracksFromPlaylistWhenPlaylistNotFoundTest() {
        val playlistToFind = "Playlist1"
        val playlistsSimplified = arrayOf(
            PlaylistSimplified.Builder().setName("Playlist3").setId("3").build()
        )

        `when`(spotifyServiceApi.executeGetListOfCurrentUsersPlaylistsRequest(any()))
            .thenReturn(playlistsSimplified)

        assertThrows<HttpClientErrorException> {
            spotifyServiceApi.getTracksFromPlaylist(playlistToFind, ACCESS_TOKEN)
        }
    }

    @Test
    internal fun refreshTokensTest() {
        `when`(spotifyServiceApi.executeAuthCodeRefreshRequest(any()))
            .thenReturn(TokensPair(ACCESS_TOKEN, REFRESH_TOKEN))
        val actualTokensPair = spotifyServiceApi.refreshTokens(REFRESH_TOKEN)
        assertEquals(ACCESS_TOKEN, actualTokensPair?.accessToken)
        assertEquals(REFRESH_TOKEN, actualTokensPair?.refreshToken)
    }

    @Test
    internal fun refreshTokensWhenApiExceptionThrownTest() {
        `when`(spotifyServiceApi.executeAuthCodeRefreshRequest(any()))
            .thenThrow(SpotifyWebApiException::class.java)
        val actualTokensPair = spotifyServiceApi.refreshTokens(REFRESH_TOKEN)
        assertEquals(null, actualTokensPair)
    }

    @Test
    internal fun refreshTokensWhenIOExceptionThrownTest() {
        `when`(spotifyServiceApi.executeAuthCodeRefreshRequest(any()))
            .thenThrow(IOException::class.java)
        val actualTokensPair = spotifyServiceApi.refreshTokens(REFRESH_TOKEN)
        assertEquals(null, actualTokensPair)
    }

    @Test
    internal fun getPlaylistsListTest() {
        val playlistsSimplified = arrayOf(
            PlaylistSimplified.Builder().setName("Playlist1").setId("1").build(),
            PlaylistSimplified.Builder().setName("Playlist2").setId("2").build(),
            PlaylistSimplified.Builder().setName("Playlist3").setId("3").build()
        )
        val expectedPlaylistList = listOf("Playlist1", "Playlist2", "Playlist3")

        `when`(spotifyServiceApi.executeGetListOfCurrentUsersPlaylistsRequest(any()))
            .thenReturn(playlistsSimplified)
        val actualPlaylistList = spotifyServiceApi.getPlaylistsList(ACCESS_TOKEN)
        assertEquals(expectedPlaylistList, actualPlaylistList)
    }

    companion object {
        private val SPOTIFY_TRACKS = listOf(
            Track.Builder()
                .setName("trackName1")
                .setArtists(ArtistSimplified.Builder().setName("trackArtist1").build())
                .setAlbum(AlbumSimplified.Builder().setName("trackAlbum1").build())
                .setId("1")
                .build(),
            Track.Builder()
                .setName("trackName2")
                .setArtists(ArtistSimplified.Builder().setName("trackArtist2").build())
                .setAlbum(AlbumSimplified.Builder().setName("trackAlbum2").build())
                .setId("2")
                .build(),
            Track.Builder()
                .setName("trackName3")
                .setArtists(ArtistSimplified.Builder().setName("trackArtist3").build())
                .setAlbum(AlbumSimplified.Builder().setName("trackAlbum3").build())
                .setId("3")
                .build()
        )
        private val TRACKS = listOf(
            MyTrack("trackName1", "trackArtist1", "trackAlbum1"),
            MyTrack("trackName2", "trackArtist2", "trackAlbum2"),
            MyTrack("trackName3", "trackArtist3", "trackAlbum3"),
        )

        private const val BASE_OAUTH_URL = "https://accounts.spotify.com/authorize?"
        private const val RESPONSE_TYPE = "code"
        private const val CLIENT_ID = "test_client_id"
        private const val CLIENT_SECRET = "test_client_secret"
        private const val SCOPE = "user-library-read%20user-top-read%20playlist-read-private"
        private const val REDIRECT_URL = "test_redirect_url"
        private const val STATE = "test_state"
        private const val AUTH_CODE = "test_auth_code"
        private const val ACCESS_TOKEN = "test_access_token"
        private const val REFRESH_TOKEN = "test_refresh_token"
        private const val PLAYLIST_NAME = "Музыка из Spotify"
    }
}