package ru.spbstu.trkpo.musicservice.api.impl

import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi

@Service
class MusicServiceApiAggregator(
    val musicServiceApi: MusicServiceApi
)