package ru.spbstu.trkpo.musicservice.service

import java.util.*

interface AuthService {
    fun getOAuthUrl(): String
    fun authorize(authCode: String): UUID
    fun authorize(authCode: String, uuid: UUID)
}