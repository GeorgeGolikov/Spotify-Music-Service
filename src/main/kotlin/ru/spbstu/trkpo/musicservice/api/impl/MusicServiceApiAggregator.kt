package ru.spbstu.trkpo.musicservice.api.impl

import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import java.io.FileInputStream
import java.util.*

@Service
class MusicServiceApiAggregator() {
    lateinit var musicServiceApi: MusicServiceApi

    init {
        val properties = Properties()
        properties.load(FileInputStream("src/main/resources/application.properties"))
        val serviceName = properties.getProperty("music-service")
        if (serviceName == "spotify") musicServiceApi = SpotifyApi()
        if (serviceName == "apple") musicServiceApi = AppleMusicApi()
    }
}