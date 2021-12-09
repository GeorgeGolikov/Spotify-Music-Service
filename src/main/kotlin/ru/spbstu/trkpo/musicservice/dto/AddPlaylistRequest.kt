package ru.spbstu.trkpo.musicservice.dto

import java.util.*

data class AddPlaylistRequest(private val userId: UUID, private val playlistName: String, private val tracks: List<Track>)
