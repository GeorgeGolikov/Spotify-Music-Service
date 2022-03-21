package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.AdditionalMatchers.not
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.entity.TokensInfo
import ru.spbstu.trkpo.musicservice.service.impl.AuthServiceImpl
import ru.spbstu.trkpo.musicservice.service.impl.SyncPointServiceImpl
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

fun postgres(imageName: String, opts: JdbcDatabaseContainer<Nothing>.() -> Unit) =
    PostgreSQLContainer<Nothing>(DockerImageName.parse(imageName)).apply(opts)

@Testcontainers
//@ExtendWith(SpringExtension::class)
@SpringBootTest
//@ExtendWith(MockitoExtension::class)
//@ContextConfiguration(classes = [(IntegrationTests.Initializer::class)])
//@ActiveProfiles("integration-test")
class IntegrationDBTests(
    @Autowired val authService: AuthServiceImpl,
    @Autowired val syncPointService: SyncPointServiceImpl
) {
    @MockBean
    private lateinit var musicServiceApi: MusicServiceApi

    @Test
    internal fun `when got tokens while registering, they are persisted in db`() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        authService.register(AUTH_CODE)

        val resultSet = connection.createStatement().executeQuery("select * from tokens_info;")

        if (resultSet.next()) {
            val resultAccessToken = resultSet.getString("access_token")
            val resultRefreshToken = resultSet.getString("refresh_token")
            assertEquals(TOKENS_PAIR.accessToken, resultAccessToken)
            assertEquals(TOKENS_PAIR.refreshToken, resultRefreshToken)
        } else {
            fail("Not persisted in db")
        }
    }

    @Test
    internal fun `when didn't get tokens while registering, they are not persisted in db`() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(null)
        authService.register(AUTH_CODE)

        val resultSet = connection.createStatement().executeQuery("select * from tokens_info;")

        assertFalse(resultSet.next())
    }

    @Test
    internal fun `when got tokens while authorizing, they are updated in db`() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
            .thenReturn(TOKENS_PAIR_NEW)
        val guid = authService.register(AUTH_CODE)
        authService.authorize(AUTH_CODE, guid!!)

        val resultSet = connection.createStatement().executeQuery("select * from tokens_info;")

        if (resultSet.next()) {
            val guidFromDB = resultSet.getString("user_id")
            val resultAccessToken = resultSet.getString("access_token")
            val resultRefreshToken = resultSet.getString("refresh_token")
            assertEquals(guid.toString(), guidFromDB)
            assertEquals(TOKENS_PAIR_NEW.accessToken, resultAccessToken)
            assertEquals(TOKENS_PAIR_NEW.refreshToken, resultRefreshToken)
            assertFalse(resultSet.next())
        } else {
            fail("Not persisted in db")
        }
    }

    @Test
    internal fun `when didn't get tokens while authorizing, they are not updated in db`() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
            .thenReturn(null)
        val guid = authService.register(AUTH_CODE)
        authService.authorize(AUTH_CODE, guid!!)

        val resultSet = connection.createStatement().executeQuery("select * from tokens_info;")

        if (resultSet.next()) {
            val resultAccessToken = resultSet.getString("access_token")
            val resultRefreshToken = resultSet.getString("refresh_token")
            assertEquals(TOKENS_PAIR.accessToken, resultAccessToken)
            assertEquals(TOKENS_PAIR.refreshToken, resultRefreshToken)
            assertFalse(resultSet.next())
        } else {
            fail("Not persisted in db")
        }
    }

    @Test
    internal fun `when getting playlist, db returns tokens or null`() {
        val expectedPlaylist = ReturnedPlaylist(PLAYLIST_NAME, listOf())
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        `when`(musicServiceApi.getTracksFromPlaylist(anyString(), eq(TOKENS_PAIR.accessToken)))
            .thenReturn(expectedPlaylist)
        `when`(musicServiceApi.getTracksFromPlaylist(anyString(), not(eq(TOKENS_PAIR.accessToken))))
            .thenReturn(null)
        val guid = authService.register(AUTH_CODE)
        val returnedPlaylist = syncPointService.getPlaylist(PLAYLIST_NAME, guid!!)
        val returnedNullPlaylist = syncPointService.getPlaylist(PLAYLIST_NAME, UUID.randomUUID())
        assertEquals(expectedPlaylist, returnedPlaylist)
        assertNull(returnedNullPlaylist)
    }

    @Test
    internal fun `container is up and running`() {
        assertTrue(container.isRunning)
    }

    @AfterEach
    internal fun cleanup() {
        connection.createStatement().execute("truncate table tokens_info;")
    }

    companion object {
        @Container
        val container = postgres("postgres:13-alpine") {
            withDatabaseName("music-service-integration-test")
            withUsername("postgres")
            withPassword("postgres")
        }

        @JvmStatic
        @DynamicPropertySource
        internal fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }

        @JvmStatic
        @BeforeAll
        internal fun getJdbcConnectionWithContainer() {
            connection = DriverManager.getConnection(container.jdbcUrl, container.username, container.password)
        }
        private lateinit var connection: Connection

        private const val TG_BOT_ID = "tg-id"
        private const val URL = "test-url"
        private const val AUTH_CODE = "authCode"
        private const val PLAYLIST_NAME = "Playlist"
        private val TOKENS_PAIR = TokensPair("Access", "Refresh")
        private val TOKENS_PAIR_NEW = TokensPair("Access1", "Refresh1")
        private val GUID = UUID.randomUUID()
        private val USER_INFO = TokensInfo().apply {
            userId = GUID
            accessToken = TOKENS_PAIR.accessToken
            refreshToken = TOKENS_PAIR.refreshToken
        }
    }
}