package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import ru.spbstu.trkpo.musicservice.controller.AuthController
import ru.spbstu.trkpo.musicservice.dto.AuthRequest
import ru.spbstu.trkpo.musicservice.dto.AuthResponse
import ru.spbstu.trkpo.musicservice.dto.GetUrlRequest
import ru.spbstu.trkpo.musicservice.dto.GetUrlResponse
import ru.spbstu.trkpo.musicservice.service.impl.AuthServiceImpl
import java.util.*

@ExtendWith(MockitoExtension::class)
class AuthControllerTest {
    @Mock
    private lateinit var authService: AuthServiceImpl

    @InjectMocks
    private lateinit var authController: AuthController

    @Test
    internal fun getOAuthUrlTest() {
        val request = GetUrlRequest("tg-id")
        val url = "test-url"
        val expectedResponse = ResponseEntity(
            GetUrlResponse(url),
            HttpStatus.OK
        )
        `when`(authService.getOAuthUrl(anyString()))
            .thenReturn("test-url")
        val actualResponse = authController.getOAuthUrl(request)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenAuthorizedTest() {
        val guid = UUID.randomUUID()
        val authCode = "authCode"
        val authRequest = AuthRequest(authCode, guid)
        val expectedResponse = ResponseEntity(
            AuthResponse(guid),
            HttpStatus.OK
        )
        `when`(authService.authorize(anyString(), eq(guid)))
            .thenReturn(true)
        val actualResponse = authController.register(authRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenNotAuthorizedTest() {
        val guid = UUID.randomUUID()
        val authCode = "authCode"
        val authRequest = AuthRequest(authCode, guid)
        val expectedResponse = badRequest().build<AuthResponse>()
        `when`(authService.authorize(anyString(), eq(guid)))
            .thenReturn(false)
        val actualResponse = authController.register(authRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenUserIdNullAndAuthCodeCorrectTest() {
        val guid = UUID.randomUUID()
        val authCode = "authCode"
        val authRequest = AuthRequest(authCode, null)
        val expectedResponse = ResponseEntity(
            AuthResponse(guid),
            HttpStatus.OK
        )
        `when`(authService.register(anyString()))
            .thenReturn(guid)
        val actualResponse = authController.register(authRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenUserIdNullAndAuthCodeNotCorrectTest() {
        val authCode = "authCode"
        val authRequest = AuthRequest(authCode, null)
        val expectedResponse = badRequest().build<AuthResponse>()
        `when`(authService.register(anyString()))
            .thenReturn(null)
        val actualResponse = authController.register(authRequest)
        assertEquals(expectedResponse, actualResponse)
    }
}