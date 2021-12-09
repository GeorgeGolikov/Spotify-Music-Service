package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.spbstu.trkpo.musicservice.service.AuthService
import java.util.*

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @GetMapping("/authUrl")
    fun getOAuthUrl(): ResponseEntity<String> {
        /*
        * TODO: implement
        * */
        return ResponseEntity(authService.getOAuthUrl(), HttpStatus.OK)
    }

    @PostMapping("/register")
    fun register(@RequestHeader("Authorization") authCode: String): ResponseEntity<UUID> {
        /*
        * TODO: implement
        * */
        return ResponseEntity(authService.register(authCode), HttpStatus.OK)
    }

    @PostMapping("/register/{id}")
    fun register(@RequestHeader("Authorization") authCode: String,
                 @PathVariable("id") uuid: UUID): ResponseEntity<HttpStatus> {
        /*
        * TODO: implement
        * */
        authService.register(authCode, uuid)
        return ResponseEntity.ok().build()
    }
}