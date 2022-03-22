package ru.spbstu.trkpo.musicservice

import org.hamcrest.Matchers.`is`
import org.hamcrest.core.AnyOf
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.entity.TokensInfo
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["application-api-integration-test.properties"])
class IntegrationApiTest(
    @Autowired val mockMvc: MockMvc
) {
    @MockBean
    private lateinit var musicServiceApi: MusicServiceApi

    @MockBean
    private lateinit var tokensInfo: TokensInfoDao

    @Test
    internal fun getOAuthUrlTest() {
        val requestBody = "{\"tgBotId\": \"testId\"}"
        `when`(musicServiceApi.getUrl(anyString()))
            .thenReturn("Url")
        val result = mockMvc.perform(
            post("/authUrl")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
        val json = JSONObject(result.response.contentAsString)
        val urlString = json.getString("url")
        assertNotNull(urlString)
    }

    @Test
    internal fun registerTest() {
        val requestBody = "{\"authCode\": \"testCode\"}"
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(tokensInfo.save(any()))
            .thenReturn(null)
        mockMvc.perform(
            post("/register")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().`is`(AnyOf(
                    `is`(200),
                    `is`(400)
                )),
                content().contentType(MediaType.APPLICATION_JSON_VALUE)
            )
    }

    @Test
    internal fun authTest() {
        val requestBody = "{\"authCode\": \"testCode\", \"userId\": \"testId\"}"
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(tokensInfo.save(any()))
            .thenReturn(null)
        mockMvc.perform(
            post("/register")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().`is`(AnyOf(
                    `is`(200),
                    `is`(400)
                )),
                content().contentType(MediaType.APPLICATION_JSON_VALUE)
            )
    }

    @Test
    internal fun getPlaylistTest() {
        val playlist = ReturnedPlaylist(NAME, listOf())
        val requestBody = "{\"guid\": \"cf297a20-0f22-44bf-b10e-e3f64ba7e0c4\", \"name\": \"Playlist\"}"
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getTracksFromPlaylist(anyString(), anyString()))
            .thenReturn(playlist)
        mockMvc.perform(
            post("/playlist")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().`is`(AnyOf(
                    `is`(200),
                    `is`(401),
                    `is`(404)
                )),
                content().contentType(MediaType.APPLICATION_JSON_VALUE)
            )
    }

    @Test
    internal fun getPlaylistsListTest() {
        val requestBody = "{\"guid\": \"cf297a20-0f22-44bf-b10e-e3f64ba7e0c4\"}"
        `when`(tokensInfo.findByUserId(any()))
            .thenReturn(USER_INFO)
        `when`(musicServiceApi.getPlaylistsList(anyString()))
            .thenReturn(PLAYLISTS_LIST)
        mockMvc.perform(
            post("/playlistsList")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().`is`(AnyOf(
                    `is`(200),
                    `is`(401)
                )),
                content().contentType(MediaType.APPLICATION_JSON_VALUE)
            )
    }

    companion object {
        private const val NAME = "Playlist"
        private val PLAYLISTS_LIST = listOf("Playlist1", "Playlist2", "Playlist3")
        private val GUID = UUID.randomUUID()
        private val TOKENS_PAIR = TokensPair("Access", "Refresh")
        private val USER_INFO = TokensInfo().apply {
            userId = GUID
            accessToken = TOKENS_PAIR.accessToken
            refreshToken = TOKENS_PAIR.refreshToken
        }
    }
}