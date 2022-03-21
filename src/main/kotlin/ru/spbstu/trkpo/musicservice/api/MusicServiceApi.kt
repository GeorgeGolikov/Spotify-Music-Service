package ru.spbstu.trkpo.musicservice.api

import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair

interface MusicServiceApi {
    fun getUrl(tgBotId: String): String
    fun register(authCode: String): TokensPair?
    @Throws(Exception::class)
    fun getTracksFromPlaylist(name: String?, accessToken: String?): ReturnedPlaylist?
    fun getSavedTracksPlaylistName(): String
    fun getCustomPlaylistName(name: String?): String
    fun refreshTokens(refreshToken: String?): TokensPair?
    @Throws(Exception::class)
    fun getPlaylistsList(accessToken: String?): List<String>?

    fun getReadPlaylistScopes(): String
}