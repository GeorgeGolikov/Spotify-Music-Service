package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import ru.spbstu.trkpo.musicservice.dto.GetPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.service.SyncPointService

@RestController
class SyncPointController {
    @Autowired
    private lateinit var syncPointService: SyncPointService

    @PostMapping(
        "/playlist",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getPlaylist(@RequestBody getPlaylistRequest: GetPlaylistRequest): ResponseEntity<ReturnedPlaylist> {
        return try {
            val playlist = syncPointService.getPlaylist(getPlaylistRequest.name, getPlaylistRequest.userId)
            if (playlist != null) ResponseEntity(playlist, HttpStatus.OK)
            else ResponseEntity(HttpStatus.UNAUTHORIZED)
        } catch (e: HttpClientErrorException) {
            ResponseEntity(e.statusCode)
        }
    }

    @PostMapping(
        "/playlistsList",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getPlaylistsList(@RequestBody getPlaylistRequest: GetPlaylistRequest): ResponseEntity<List<String>> {
        val playlists = syncPointService.getPlaylistsList(getPlaylistRequest.userId)
        return if (playlists != null) ResponseEntity(playlists, HttpStatus.OK)
        else ResponseEntity(HttpStatus.UNAUTHORIZED)
    }
}