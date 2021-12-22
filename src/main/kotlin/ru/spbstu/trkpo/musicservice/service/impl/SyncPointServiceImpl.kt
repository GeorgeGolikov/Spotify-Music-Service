package ru.spbstu.trkpo.musicservice.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.spbstu.trkpo.musicservice.api.impl.MusicServiceApiAggregator
import ru.spbstu.trkpo.musicservice.dao.TokensInfoDao
import ru.spbstu.trkpo.musicservice.dto.*
import ru.spbstu.trkpo.musicservice.service.SyncPointService
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import java.io.IOException
import java.util.*

@Service
class SyncPointServiceImpl: SyncPointService {
    @Autowired
    private lateinit var tokensInfo: TokensInfoDao

    @Autowired
    private lateinit var musicServiceApiAggregator: MusicServiceApiAggregator

    override fun getPlaylist(name: String?, userId: UUID): ReturnedPlaylist? {
        val service = musicServiceApiAggregator.musicServiceApi
        val tokens = tokensInfo.findByUserId(userId) ?: return null

        val accessToken = tokens.accessToken

        return try {
            service.getTracksFromPlaylist(name, accessToken)
        } catch (e: SpotifyWebApiException) {
            val refreshedTokens = service.refreshTokens(tokens.refreshToken)
            return if (refreshedTokens != null) {
                tokens.accessToken = refreshedTokens.accessToken
                tokens.refreshToken = refreshedTokens.refreshToken
                tokensInfo.save(tokens)
                service.getTracksFromPlaylist(name, refreshedTokens.accessToken)
            } else null
        } catch (e: IOException) {
            null
        }
    }

    override fun getPlaylistsList(userId: UUID): List<String>? {
        val service = musicServiceApiAggregator.musicServiceApi
        val tokens = tokensInfo.findByUserId(userId) ?: return null

        val accessToken = tokens.accessToken

        return try {
            service.getPlaylistsList(accessToken)
        } catch (e: SpotifyWebApiException) {
            val refreshedTokens = service.refreshTokens(tokens.refreshToken)
            return if (refreshedTokens != null) {
                tokens.accessToken = refreshedTokens.accessToken
                tokens.refreshToken = refreshedTokens.refreshToken
                tokensInfo.save(tokens)
                service.getPlaylistsList(refreshedTokens.accessToken)
            } else null
        } catch (e: IOException) {
            null
        }
    }

    /*
    * TODO: implement
    * */
    override fun addPlaylist(request: AddPlaylistRequest) {

    }
}