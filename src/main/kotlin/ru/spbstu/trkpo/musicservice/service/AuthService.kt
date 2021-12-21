package ru.spbstu.trkpo.musicservice.service

import java.util.*

interface AuthService {
    fun getOAuthUrl(tgBotId: String): String
    fun register(authCode: String): UUID
    fun authorize(authCode: String, uuid: UUID)
}