package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import ru.spbstu.trkpo.musicservice.service.AuthService

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService
}