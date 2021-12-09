package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.spbstu.trkpo.musicservice.dto.Track
import ru.spbstu.trkpo.musicservice.service.SyncPointService
import java.util.*

@RestController
class SyncPointController {
    @Autowired
    private lateinit var syncPointService: SyncPointService

    @GetMapping("/playlist/{name}")
    fun getPlaylist(@PathVariable(name = "name") name: String,
                    @RequestParam("id") uuid: UUID): ResponseEntity<List<Track>> {
        /*
        * TODO: implement
        * */
        return ResponseEntity(ArrayList<Track>(5), HttpStatus.OK)
    }

    @PostMapping("/playlist/{name}")
    fun addPlaylist(@PathVariable("name") name: String,
                    @RequestParam("id") uuid: UUID,
                    @RequestBody listOfTracks: List<Track>): ResponseEntity<HttpStatus> {
        /*
        * TODO: implement
        * */
        return ResponseEntity.ok().build()
    }
}