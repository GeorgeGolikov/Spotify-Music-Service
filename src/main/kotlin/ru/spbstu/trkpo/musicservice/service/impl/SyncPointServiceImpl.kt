package ru.spbstu.trkpo.musicservice.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.api.MusicServiceApi
import ru.spbstu.trkpo.musicservice.api.impl.MusicServiceApiAggregator
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.dto.*
import ru.spbstu.trkpo.musicservice.service.SyncPointService
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import java.io.IOException
import java.util.*
import javax.annotation.PostConstruct

@Service
class SyncPointServiceImpl: SyncPointService {
    @Autowired
    private lateinit var tokensInfo: TokensInfoDao

    @Autowired
    private lateinit var musicServiceApiAggregator: MusicServiceApiAggregator

    private lateinit var musicServiceApi: MusicServiceApi

    @PostConstruct
    private fun init() {
        musicServiceApi = musicServiceApiAggregator.musicServiceApi
    }

    override fun getPlaylist(name: String?, userId: UUID): ReturnedPlaylist? {
        val tokens = tokensInfo.findByUserId(userId) ?: return null

        val accessToken = tokens.accessToken

        return try {
            musicServiceApi.getTracksFromPlaylist(name, accessToken)
        } catch (e: SpotifyWebApiException) {
            val refreshedTokens = musicServiceApi.refreshTokens(tokens.refreshToken)
            return if (refreshedTokens != null) {
                tokens.accessToken = refreshedTokens.accessToken
                tokens.refreshToken = refreshedTokens.refreshToken
                tokensInfo.save(tokens)
                musicServiceApi.getTracksFromPlaylist(name, refreshedTokens.accessToken)
            } else null
        } catch (e: IOException) {
            null
        }
    }

    override fun getPlaylistsList(userId: UUID): List<String>? {
        val tokens = tokensInfo.findByUserId(userId) ?: return null

        val accessToken = tokens.accessToken

        return try {
            musicServiceApi.getPlaylistsList(accessToken)
        } catch (e: SpotifyWebApiException) {
            val refreshedTokens = musicServiceApi.refreshTokens(tokens.refreshToken)
            return if (refreshedTokens != null) {
                tokens.accessToken = refreshedTokens.accessToken
                tokens.refreshToken = refreshedTokens.refreshToken
                tokensInfo.save(tokens)
                musicServiceApi.getPlaylistsList(refreshedTokens.accessToken)
            } else null
        } catch (e: IOException) {
            null
        }
    }
}