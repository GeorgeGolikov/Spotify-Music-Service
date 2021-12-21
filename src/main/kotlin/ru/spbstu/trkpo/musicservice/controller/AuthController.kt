package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.spbstu.trkpo.musicservice.dto.AuthRequest
import ru.spbstu.trkpo.musicservice.dto.GetUrlRequest
import ru.spbstu.trkpo.musicservice.dto.GetUrlResponse
import ru.spbstu.trkpo.musicservice.service.AuthService
import java.util.*

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @GetMapping("/authUrl")
    fun getOAuthUrl(@RequestBody request: GetUrlRequest): ResponseEntity<GetUrlResponse> {
        /*
        * TODO: implement
        * */
        return ResponseEntity(
            GetUrlResponse(authService.getOAuthUrl(request.tgBotId)),
            HttpStatus.OK
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody authCode: String): ResponseEntity<UUID> {
        /*
        * TODO: implement
        * */
        return ResponseEntity(authService.register(authCode), HttpStatus.OK)
    }

    @PostMapping("/authorize")
    fun authorize(@RequestBody authRequest: AuthRequest): ResponseEntity<HttpStatus> {
        /*
        * TODO: implement
        * */
        authService.authorize(authRequest.authCode, authRequest.userId)
        return ResponseEntity.ok().build()
    }
}