package ru.spbstu.trkpo.musicservice.api.impl

import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import java.util.*

class AppleMusicServiceApi: MusicServiceApi {
    override fun getUrl(tgBotId: String): String {
        TODO("Not yet implemented")
    }

    override fun register(authCode: String): TokensPair? {
        TODO("Not yet implemented")
    }

    override fun setProperties(properties: Properties) {
        TODO("Not yet implemented")
    }

    override fun getReadPlaylistScopes(): String {
        TODO("Not yet implemented")
    }
}