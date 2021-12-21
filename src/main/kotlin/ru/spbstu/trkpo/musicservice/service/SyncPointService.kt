package ru.spbstu.trkpo.musicservice.service

import ru.spbstu.trkpo.musicservice.dto.AddPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.GetPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist

interface SyncPointService {
    fun getPlaylist(request: GetPlaylistRequest): ReturnedPlaylist
    fun addPlaylist(request: AddPlaylistRequest)
}