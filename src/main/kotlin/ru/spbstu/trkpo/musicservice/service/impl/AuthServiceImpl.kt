package ru.spbstu.trkpo.musicservice.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.service.AuthService

@Service
class AuthServiceImpl: AuthService {
    @Autowired
    private lateinit var tokensInfo: TokensInfoDao

    override fun getOAuthUrl(): String {

    }
}