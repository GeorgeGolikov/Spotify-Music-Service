package ru.spbstu.trkpo.musicservice.service

import ru.spbstu.trkpo.musicservice.dto.AddPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.GetPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.Track

interface SyncPointService {
    fun getPlaylist(request: GetPlaylistRequest): List<Track>
    fun addPlaylist(request: AddPlaylistRequest)
}