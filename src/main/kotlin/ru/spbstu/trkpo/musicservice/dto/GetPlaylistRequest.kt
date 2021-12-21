package ru.spbstu.trkpo.musicservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetPlaylistRequest(val userId: UUID, val name: String?)
