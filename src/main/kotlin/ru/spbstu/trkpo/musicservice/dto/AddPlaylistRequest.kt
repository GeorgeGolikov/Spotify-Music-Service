package ru.spbstu.trkpo.musicservice.dto

import java.util.*

data class AddPlaylistRequest(val userId: UUID, val playlistName: String, val tracks: List<Track>)
