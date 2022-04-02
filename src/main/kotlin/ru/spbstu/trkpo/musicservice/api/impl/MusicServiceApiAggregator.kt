package ru.spbstu.trkpo.musicservice.api.impl

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.annotation.Generated
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import javax.annotation.PostConstruct

@Service
@ConfigurationProperties(prefix = "music-service")
class MusicServiceApiAggregator(
    private final val spotifyServiceApi: SpotifyServiceApi,
    private final val spotifyServiceTestModeApi: SpotifyServiceTestModeApi
) {
    lateinit var type: String
    lateinit var musicServiceApi: MusicServiceApi

    @Generated
    @PostConstruct
    private fun init() {
        musicServiceApi = if (type == "test") {
            spotifyServiceTestModeApi
        } else {
            spotifyServiceApi
        }
    }
}