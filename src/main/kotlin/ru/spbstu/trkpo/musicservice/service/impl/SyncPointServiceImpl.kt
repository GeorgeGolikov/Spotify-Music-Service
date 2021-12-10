package ru.spbstu.trkpo.musicservice.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.api.impl.MusicServiceApiAggregator
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.dto.AddPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.GetPlaylistRequest
import ru.spbstu.trkpo.musicservice.dto.Track
import ru.spbstu.trkpo.musicservice.service.SyncPointService
import java.util.*

@Service
class SyncPointServiceImpl: SyncPointService {
    @Autowired
    private lateinit var tokensInfo: TokensInfoDao

    @Autowired
    private lateinit var musicServiceApiAggregator: MusicServiceApiAggregator

    /*
    * TODO: implement
    * */
    override fun getPlaylist(request: GetPlaylistRequest): List<Track> {
        return ArrayList()
    }

    /*
    * TODO: implement
    * */
    override fun addPlaylist(request: AddPlaylistRequest) {

    }
}