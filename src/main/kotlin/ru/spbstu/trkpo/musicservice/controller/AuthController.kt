package ru.spbstu.trkpo.musicservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.spbstu.trkpo.musicservice.dto.*
import ru.spbstu.trkpo.musicservice.service.AuthService

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("/authUrl", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getOAuthUrl(@RequestBody request: GetUrlRequest): ResponseEntity<GetUrlResponse> {
        return ResponseEntity(
            GetUrlResponse(authService.getOAuthUrl(request.tgBotId)),
            HttpStatus.OK
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        if (authRequest.userId == null) {
            val guid = authService.register(authRequest.authCode) ?: return ResponseEntity.badRequest().build()
            return ResponseEntity(
                AuthResponse(guid),
                HttpStatus.OK
            )
        }
        val authorized = authService.authorize(authRequest.authCode, authRequest.userId)
        return if (authorized) ResponseEntity(
            AuthResponse(authRequest.userId),
            HttpStatus.OK
        )
        else ResponseEntity.badRequest().build()
    }
}