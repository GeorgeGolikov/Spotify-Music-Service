package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import ru.spbstu.trkpo.musicservice.service.SyncPointService

@RestController
class SyncPointController {
    @Autowired
    private lateinit var syncPointService: SyncPointService
}