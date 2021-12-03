package ru.spbstu.trkpo.musicservice.dto

import java.util.*

data class PlaylistRequest(private val userId: UUID, private val playlistName: String)
