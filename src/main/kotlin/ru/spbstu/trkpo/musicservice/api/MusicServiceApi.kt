package ru.spbstu.trkpo.musicservice.api

import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair
import java.util.*

interface MusicServiceApi {
    fun getUrl(tgBotId: String): String
    fun register(authCode: String): TokensPair?
    fun getTracksFromPlaylist(name: String?, accessToken: String?): ReturnedPlaylist?
    fun getSavedTracksPlaylistName(): String
    fun refreshTokens(refreshToken: String?): TokensPair?

    fun setProperties(properties: Properties)
    fun getReadPlaylistScopes(): String
}