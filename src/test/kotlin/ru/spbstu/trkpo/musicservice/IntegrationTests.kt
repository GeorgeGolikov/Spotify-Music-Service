package ru.spbstu.trkpo.musicservice

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
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
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.entity.TokensInfo
import ru.spbstu.trkpo.musicservice.service.impl.AuthServiceImpl
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
class IntegrationTests(
    @Autowired val authService: AuthServiceImpl
) {
    @MockBean
    private lateinit var musicServiceApi: MusicServiceApi

    @Test
    internal fun registerTest() {
        `when`(musicServiceApi.register(anyString()))
            .thenReturn(TOKENS_PAIR)
        val actualResponse = authService.register(AUTH_CODE)
        Assertions.assertNotNull(actualResponse)

        val jdbcUrl = container.jdbcUrl
        val username = container.username
        val password = container.password
        val connection = DriverManager.getConnection(jdbcUrl, username, password)
        val resultSet = connection.createStatement().executeQuery("select * from tokens_info;")

        if (resultSet.next()) {
            val result = resultSet.getString(1)
            print(result)
        }
    }

    @Test
    internal fun `container is up and running`() {
        Assertions.assertTrue(container.isRunning)
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
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }

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