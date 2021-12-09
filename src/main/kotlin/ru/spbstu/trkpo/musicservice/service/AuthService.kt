package ru.spbstu.trkpo.musicservice.service

import java.util.*

interface AuthService {
    fun getOAuthUrl(): String
    fun register(authCode: String): UUID
    fun register(authCode: String, uuid: UUID)
}