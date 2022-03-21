package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.entity.TokensInfo
import ru.spbstu.trkpo.musicservice.service.impl.SyncPointServiceImpl
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import java.io.IOException
import java.util.*

@ExtendWith(MockitoExtension::class)
class SyncPointServiceTest {
    @Mock
    private lateinit var tokensInfo: TokensInfoDao

    @Mock
    private lateinit var musicServiceApi: MusicServiceApi

    @InjectMocks
    private lateinit var syncPointService: SyncPointServiceImpl

    @Test
    internal fun getPlaylistTest() {
        val playlist = ReturnedPlaylist(NAME, listOf())
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getTracksFromPlaylist(anyString(), anyString()))
            .thenReturn(playlist)
        val actualResponse = syncPointService.getPlaylist(NAME, GUID)
        assertEquals(playlist, actualResponse)
    }

    @Test
    internal fun getPlaylistWhenUserNotFoundInDBTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(null)
        assertNull(syncPointService.getPlaylist(NAME, GUID))
    }

    @Test
    internal fun getPlaylistWhenIOExceptionThrownTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getTracksFromPlaylist(anyString(), anyString()))
            .thenThrow(IOException::class.java)
        val actualResponse = syncPointService.getPlaylist(NAME, GUID)
        assertNull(actualResponse)
    }

    @Test
    internal fun getPlaylistWhenSpotifyExceptionThrownTest() {
        val playlist = ReturnedPlaylist(NAME, listOf())
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getTracksFromPlaylist(anyString(), anyString()))
            .thenThrow(SpotifyWebApiException::class.java)
            .thenReturn(playlist)
        `when`(musicServiceApi.refreshTokens(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(tokensInfo.save(any()))
            .thenReturn(null)
        val actualResponse = syncPointService.getPlaylist(NAME, GUID)
        assertEquals(playlist, actualResponse)
    }

    @Test
    internal fun getPlaylistWhenSpotifyExceptionThrownAndRefreshNotValidTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getTracksFromPlaylist(anyString(), anyString()))
            .thenThrow(SpotifyWebApiException::class.java)
        `when`(musicServiceApi.refreshTokens(anyString()))
            .thenReturn(null)
        val actualResponse = syncPointService.getPlaylist(NAME, GUID)
        assertNull(actualResponse)
    }

    @Test
    internal fun getPlaylistsListTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getPlaylistsList(anyString()))
            .thenReturn(PLAYLISTS_LIST)
        val actualResponse = syncPointService.getPlaylistsList(GUID)
        assertEquals(PLAYLISTS_LIST, actualResponse)
    }

    @Test
    internal fun getPlaylistsListWhenUserNotFoundInDBTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(null)
        assertNull(syncPointService.getPlaylistsList(GUID))
    }

    @Test
    internal fun getPlaylistsListWhenIOExceptionThrownTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getPlaylistsList(anyString()))
            .thenThrow(IOException::class.java)
        val actualResponse = syncPointService.getPlaylistsList(GUID)
        assertNull(actualResponse)
    }

    @Test
    internal fun getPlaylistsListWhenSpotifyExceptionThrownTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getPlaylistsList(anyString()))
            .thenThrow(SpotifyWebApiException::class.java)
            .thenReturn(PLAYLISTS_LIST)
        `when`(musicServiceApi.refreshTokens(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(tokensInfo.save(any()))
            .thenReturn(null)
        val actualResponse = syncPointService.getPlaylistsList(GUID)
        assertEquals(PLAYLISTS_LIST, actualResponse)
    }

    @Test
    internal fun getPlaylistsListWhenSpotifyExceptionThrownAndRefreshNotValidTest() {
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getPlaylistsList(anyString()))
            .thenThrow(SpotifyWebApiException::class.java)
        `when`(musicServiceApi.refreshTokens(anyString()))
            .thenReturn(null)
        val actualResponse = syncPointService.getPlaylistsList(GUID)
        assertNull(actualResponse)
    }

    companion object {
        private const val NAME = "Playlist"
        val PLAYLISTS_LIST = listOf("Playlist1", "Playlist2", "Playlist3")
        private val TOKENS_PAIR = TokensPair("Access", "Refresh")
        private val GUID = UUID.randomUUID()
        private val USER_INFO = TokensInfo().apply {
            userId = GUID
            accessToken = TOKENS_PAIR.accessToken
            refreshToken = TOKENS_PAIR.refreshToken
        }
    }
}