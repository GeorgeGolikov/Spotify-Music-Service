package ru.spbstu.trkpo.musicservice.api.impl

import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import ru.spbstu.trkpo.musicservice.dto.Track
import java.util.*

class AppleMusicServiceApi: MusicServiceApi {
    override fun getUrl(tgBotId: String): String {
        TODO("Not yet implemented")
    }

    override fun register(authCode: String): TokensPair? {
        TODO("Not yet implemented")
    }

    override fun getTracksFromPlaylist(name: String?, accessToken: String?): ReturnedPlaylist? {
        TODO("Not yet implemented")
    }

    override fun getSavedTracksPlaylistName(): String {
        TODO("Not yet implemented")
    }

    override fun getCustomPlaylistName(name: String?): String {
        TODO("Not yet implemented")
    }

    override fun refreshTokens(refreshToken: String?): TokensPair? {
        TODO("Not yet implemented")
    }

    override fun getPlaylistsList(accessToken: String?): List<String>? {
        TODO("Not yet implemented")
    }

    override fun setProperties(properties: Properties) {
        TODO("Not yet implemented")
    }

    override fun getReadPlaylistScopes(): String {
        TODO("Not yet implemented")
    }
}