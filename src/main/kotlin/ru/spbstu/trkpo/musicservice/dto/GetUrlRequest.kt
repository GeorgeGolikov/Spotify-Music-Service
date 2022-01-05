package ru.spbstu.trkpo.musicservice.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetUrlRequest(@JsonProperty("tgBotId") val tgBotId: String)
