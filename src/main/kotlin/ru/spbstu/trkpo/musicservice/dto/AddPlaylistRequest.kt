package ru.spbstu.trkpo.musicservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddPlaylistRequest(val userId: UUID, val playlistName: String, val tracks: List<Track>)
