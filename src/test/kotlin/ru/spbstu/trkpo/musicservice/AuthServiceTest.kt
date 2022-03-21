package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.entity.TokensInfo
import ru.spbstu.trkpo.musicservice.service.impl.AuthServiceImpl
import java.util.*

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {
    @Mock
    private lateinit var tokensInfo: TokensInfoDao

    @Mock
    private lateinit var musicServiceApi: MusicServiceApi

    @InjectMocks
    private lateinit var authService: AuthServiceImpl

    @Test
    internal fun getOAuthUrlTest() {
        `when`(musicServiceApi.getUrl(anyString()))
            .thenReturn(URL)
        val actualResponse = authService.getOAuthUrl(TG_BOT_ID)
        assertEquals(URL, actualResponse)
    }

    @Test
    internal fun registerTest() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(tokensInfo.save(any()))
            .thenReturn(null)
        val actualResponse = authService.register(AUTH_CODE)
        assertNotNull(actualResponse)
    }

    @Test
    internal fun registerWhenTokensNullTest() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(null)
        val actualResponse = authService.register(AUTH_CODE)
        assertNull(actualResponse)
    }

    @Test
    internal fun authorizeTest() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(tokensInfo.save(any()))
            .thenReturn(null)
        assertTrue(authService.authorize(AUTH_CODE, GUID))
    }

    @Test
    internal fun authorizeWhenTokensNullTest() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(null)
        assertFalse(authService.authorize(AUTH_CODE, GUID))
    }

    @Test
    internal fun authorizeWhenUserNotFoundInDBTest() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(null)
        assertFalse(authService.authorize(AUTH_CODE, GUID))
    }

    companion object {
        private const val TG_BOT_ID = "tg-id"
        private const val URL = "test-url"
        private const val AUTH_CODE = "authCode"
        private val TOKENS_PAIR = TokensPair("Access", "Refresh")
        private val GUID = UUID.randomUUID()
        private val USER_INFO = TokensInfo().apply {
            userId = GUID
            accessToken = TOKENS_PAIR.accessToken
            refreshToken = TOKENS_PAIR.refreshToken
        }
    }
}