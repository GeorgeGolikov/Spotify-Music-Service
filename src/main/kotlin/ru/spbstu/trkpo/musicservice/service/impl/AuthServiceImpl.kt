package ru.spbstu.trkpo.musicservice.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.api.impl.MusicServiceApiAggregator
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.service.AuthService
import java.util.*

@Service
class AuthServiceImpl: AuthService {
    @Autowired
    private lateinit var tokensInfo: TokensInfoDao

    @Autowired
    private lateinit var musicServiceApiAggregator: MusicServiceApiAggregator

    /*
    * TODO: implement
    * */
    override fun getOAuthUrl(): String {
        return "to implement"
    }

    /*
    * TODO: implement
    * */
    override fun register(authCode: String): UUID {
        return UUID.randomUUID()
    }

    /*
    * TODO: implement
    * */
    override fun register(authCode: String, uuid: UUID) {

    }
}