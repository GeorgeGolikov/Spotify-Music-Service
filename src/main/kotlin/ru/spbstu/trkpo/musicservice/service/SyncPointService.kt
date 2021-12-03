package ru.spbstu.trkpo.musicservice.service

import ru.spbstu.trkpo.musicservice.dto.PlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.Track

interface SyncPointService {
    fun getPlaylist(request: PlaylistRequest): List<Track>
    fun addPlaylist(/*???*/)
}