package ru.spbstu.trkpo.musicservice.api.impl

import org.springframework.stereotype.Component
import ru.spbstu.trkpo.musicservice.dto.MyTrack
import ru.spbstu.trkpo.musicservice.dto.ReturnedPlaylist
import ru.spbstu.trkpo.musicservice.dto.TokensPair

@Component("spotifyServiceTestModeApi")
class SpotifyServiceTestModeApi(
    private final val spotifyServiceApiConfig: SpotifyServiceApiConfig
) : SpotifyServiceApi(spotifyServiceApiConfig) {

    override fun register(authCode: String): TokensPair? {
        return TokensPair(
            "test-access-token",
            "test-refresh-token"
        )
    }

    override fun getTracksFromPlaylist(name: String?, accessToken: String?): ReturnedPlaylist? {
        val tracks = listOf(
            MyTrack("TestName1", "Test artists1", "TestAlbum1"),
            MyTrack("TestName2", "Test artists2", "TestAlbum2"),
            MyTrack("TestName3", "Test artists3", "TestAlbum3")
        )
        return if (name == null || name == "") {
            ReturnedPlaylist(getSavedTracksPlaylistName(), tracks)
        } else {
            ReturnedPlaylist(getCustomPlaylistName(name), tracks)
        }
    }

    override fun getPlaylistsList(accessToken: String?): List<String>? {
        return listOf("TestPlaylist1", "TestPlaylist2", "TestPlaylist3")
    }
}