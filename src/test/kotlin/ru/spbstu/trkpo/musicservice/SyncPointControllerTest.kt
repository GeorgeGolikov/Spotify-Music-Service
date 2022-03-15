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
        val guid = UUID.randomUUID()
        val name = "Playlist"
        val getPlaylistRequest = GetPlaylistRequest(guid, name)
        val playlist = ReturnedPlaylist(name, listOf())
        val expectedResponse = ResponseEntity(playlist, HttpStatus.OK)
        `when`(syncPointService.getPlaylist(anyString(), eq(guid)))
            .thenReturn(playlist)
        val actualResponse = syncPointController.getPlaylist(getPlaylistRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistWhenNullTest() {
        val guid = UUID.randomUUID()
        val name = "Playlist"
        val getPlaylistRequest = GetPlaylistRequest(guid, name)
        val playlist = null
        val expectedResponse = ResponseEntity<ReturnedPlaylist>(HttpStatus.UNAUTHORIZED)
        `when`(syncPointService.getPlaylist(anyString(), eq(guid)))
            .thenReturn(playlist)
        val actualResponse = syncPointController.getPlaylist(getPlaylistRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistExceptionOccurredTest() {
        val guid = UUID.randomUUID()
        val name = "Playlist"
        val getPlaylistRequest = GetPlaylistRequest(guid, name)
        val expectedResponse = ResponseEntity<ReturnedPlaylist>(HttpStatus.FORBIDDEN)
        `when`(syncPointService.getPlaylist(anyString(), eq(guid)))
            .thenThrow(HttpClientErrorException(HttpStatus.FORBIDDEN))
        val actualResponse = syncPointController.getPlaylist(getPlaylistRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistsListTest() {
        val guid = UUID.randomUUID()
        val getPlaylistsListRequest = GetPlaylistRequest(guid, null)
        val playlists = listOf<String>()
        val expectedResponse = ResponseEntity(playlists, HttpStatus.OK)
        `when`(syncPointService.getPlaylistsList(eq(guid)))
            .thenReturn(playlists)
        val actualResponse = syncPointController.getPlaylistsList(getPlaylistsListRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun getPlaylistsListWhenNullTest() {
        val guid = UUID.randomUUID()
        val getPlaylistsListRequest = GetPlaylistRequest(guid, null)
        val playlists = null
        val expectedResponse = ResponseEntity<ReturnedPlaylist>(HttpStatus.UNAUTHORIZED)
        `when`(syncPointService.getPlaylistsList(eq(guid)))
            .thenReturn(playlists)
        val actualResponse = syncPointController.getPlaylistsList(getPlaylistsListRequest)
        assertEquals(expectedResponse, actualResponse)
    }
}