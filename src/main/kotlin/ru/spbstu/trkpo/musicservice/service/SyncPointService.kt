package ru.spbstu.trkpo.musicservice.service

import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import java.util.*

interface SyncPointService {
    fun getPlaylist(name: String?, userId: UUID): ReturnedPlaylist?
    fun getPlaylistsList(userId: UUID): List<String>?
}