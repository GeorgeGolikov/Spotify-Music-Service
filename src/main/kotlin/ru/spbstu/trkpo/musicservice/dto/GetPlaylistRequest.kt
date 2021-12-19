package ru.spbstu.trkpo.musicservice.dto

import java.util.*

data class GetPlaylistRequest(val userId: UUID, val playlistName: String)
