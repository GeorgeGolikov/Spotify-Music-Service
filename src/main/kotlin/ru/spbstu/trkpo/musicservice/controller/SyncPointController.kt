package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import ru.spbstu.trkpo.musicservice.dto.AddPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.GetPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.Track
import ru.spbstu.trkpo.musicservice.service.SyncPointService
import java.util.*

@RestController
class SyncPointController {
    @Autowired
    private lateinit var syncPointService: SyncPointService

    @GetMapping("/playlist")
    fun getPlaylist(@RequestBody getPlaylistRequest: GetPlaylistRequest): ResponseEntity<ReturnedPlaylist> {
        return try {
            val playlist = syncPointService.getPlaylist(getPlaylistRequest.name, getPlaylistRequest.userId)
            if (playlist != null) ResponseEntity(playlist, HttpStatus.OK)
            else ResponseEntity(HttpStatus.UNAUTHORIZED)
        } catch (e: HttpClientErrorException) {
            ResponseEntity(e.statusCode)
        }
    }

    @PostMapping("/playlist")
    fun addPlaylist(@RequestBody addPlaylistRequest: AddPlaylistRequest): ResponseEntity<HttpStatus> {
        /*
        * TODO: implement
        * */
        return ResponseEntity.ok().build()
    }
}