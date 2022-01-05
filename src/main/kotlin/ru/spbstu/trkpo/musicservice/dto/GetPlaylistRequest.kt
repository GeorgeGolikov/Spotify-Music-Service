package ru.spbstu.trkpo.musicservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetPlaylistRequest(
    @JsonProperty("guid") val userId: UUID,
    @JsonProperty("name") val name: String?
)
