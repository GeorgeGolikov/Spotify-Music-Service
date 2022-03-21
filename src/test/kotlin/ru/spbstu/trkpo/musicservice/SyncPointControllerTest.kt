package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import ru.spbstu.trkpo.musicservice.controller.SyncPointController
import ru.spbstu.trkpo.musicservice.dto.GetPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.service.impl.SyncPointServiceImpl
import java.util.*

@ExtendWith(MockitoExtension::class)
class SyncPointControllerTest {
    @Mock
    private lateinit var syncPointService: SyncPointServiceImpl

    @InjectMocks
    private lateinit var syncPointController: SyncPointController

    @Test
    internal fun getPlaylistTest() {
        val playlist = ReturnedPlaylist(NAME, listOf())
        val expectedResponse = ResponseEntity(playlist, HttpStatus.OK)
        `when`(syncPointService.getPlaylist(anyString(), eq(GUID)))
            .thenReturn(playlist)
        val actualResponse = syncPointController.getPlaylist(GET_PLAYLIST_REQUEST)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistWhenNullTest() {
        val playlist = null
        val expectedResponse = ResponseEntity<ReturnedPlaylist>(HttpStatus.UNAUTHORIZED)
        `when`(syncPointService.getPlaylist(anyString(), eq(GUID)))
            .thenReturn(playlist)
        val actualResponse = syncPointController.getPlaylist(GET_PLAYLIST_REQUEST)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistExceptionOccurredTest() {
        val expectedResponse = ResponseEntity<ReturnedPlaylist>(HttpStatus.FORBIDDEN)
        `when`(syncPointService.getPlaylist(anyString(), eq(GUID)))
            .thenThrow(HttpClientErrorException(HttpStatus.FORBIDDEN))
        val actualResponse = syncPointController.getPlaylist(GET_PLAYLIST_REQUEST)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistsListTest() {
        val getPlaylistsListRequest = GetPlaylistRequest(GUID, null)
        val playlists = listOf<String>()
        val expectedResponse = ResponseEntity(playlists, HttpStatus.OK)
        `when`(syncPointService.getPlaylistsList(eq(GUID)))
            .thenReturn(playlists)
        val actualResponse = syncPointController.getPlaylistsList(getPlaylistsListRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistsListWhenNullTest() {
        val getPlaylistsListRequest = GetPlaylistRequest(GUID, null)
        val playlists = null
        val expectedResponse = ResponseEntity<ReturnedPlaylist>(HttpStatus.UNAUTHORIZED)
        `when`(syncPointService.getPlaylistsList(eq(GUID)))
            .thenReturn(playlists)
        val actualResponse = syncPointController.getPlaylistsList(getPlaylistsListRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    companion object {
        private const val NAME = "Playlist"
        private val GUID = UUID.randomUUID()
        private val GET_PLAYLIST_REQUEST = GetPlaylistRequest(GUID, NAME)
    }
}