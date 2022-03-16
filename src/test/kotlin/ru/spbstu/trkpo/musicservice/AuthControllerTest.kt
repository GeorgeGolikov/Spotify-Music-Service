package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
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
        val request = GetUrlRequest(TG_BOT_ID)
        val expectedResponse = ResponseEntity(
            GetUrlResponse(URL),
            HttpStatus.OK
        )
        `when`(authService.getOAuthUrl(anyString()))
            .thenReturn(URL)
        val actualResponse = authController.getOAuthUrl(request)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenAuthorizedTest() {
        val expectedResponse = ResponseEntity(
            AuthResponse(GUID),
            HttpStatus.OK
        )
        `when`(authService.authorize(anyString(), eq(GUID)))
            .thenReturn(true)
        val actualResponse = authController.register(AUTH_REQUEST)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenNotAuthorizedTest() {
        val expectedResponse = badRequest().build<AuthResponse>()
        `when`(authService.authorize(anyString(), eq(GUID)))
            .thenReturn(false)
        val actualResponse = authController.register(AUTH_REQUEST)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenUserIdNullAndAuthCodeCorrectTest() {
        val authRequest = AuthRequest(AUTH_CODE, null)
        val expectedResponse = ResponseEntity(
            AuthResponse(GUID),
            HttpStatus.OK
        )
        `when`(authService.register(anyString()))
            .thenReturn(GUID)
        val actualResponse = authController.register(authRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    internal fun registerWhenUserIdNullAndAuthCodeNotCorrectTest() {
        val authRequest = AuthRequest(AUTH_CODE, null)
        val expectedResponse = badRequest().build<AuthResponse>()
        `when`(authService.register(anyString()))
            .thenReturn(null)
        val actualResponse = authController.register(authRequest)
        assertEquals(expectedResponse, actualResponse)
    }

    companion object {
        private const val TG_BOT_ID = "tg-id"
        private const val URL = "test-url"
        private const val AUTH_CODE = "authCode"
        private val GUID = UUID.randomUUID()
        private val AUTH_REQUEST = AuthRequest(AUTH_CODE, GUID)
    }
}