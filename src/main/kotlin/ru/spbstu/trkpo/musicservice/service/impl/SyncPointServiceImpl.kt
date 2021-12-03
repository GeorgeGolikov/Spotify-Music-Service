package ru.spbstu.trkpo.musicservice.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.dto.PlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.Track
import ru.spbstu.trkpo.musicservice.service.SyncPointService

@Service
class SyncPointServiceImpl: SyncPointService {
    @Autowired
    private lateinit var tokensInfo: TokensInfoDao

    override fun getPlaylist(request: PlaylistRequest): List<Track> {

    }
}