package ru.spbstu.trkpo.musicservice.api

import java.util.*

interface MusicServiceApi {
    fun getUrl(tgBotId: String): String

    fun setProperties(properties: Properties)
    fun getReadPlaylistScopes(): String
}