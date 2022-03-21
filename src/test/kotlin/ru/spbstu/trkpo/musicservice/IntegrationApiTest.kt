package ru.spbstu.trkpo.musicservice

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("application-api-integration-test.properties")
class IntegrationApiTest(
    @Autowired val mockMvc: MockMvc
) {
    @MockBean
    private lateinit var musicServiceApi: MusicServiceApi

    @MockBean
    private lateinit var tokensInfo: TokensInfoDao

    @Test
    internal fun `when oauth url requested, return 200 and url in string representation`() {
        `when`(musicServiceApi.getUrl(anyString()))
            .thenReturn("Url")
        val result = mockMvc.perform(get("/authUrl"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
        val json = JSONObject(result.response.contentAsString)
        val urlString = json.getString("url")
        assertEquals("Url", urlString)
    }
}