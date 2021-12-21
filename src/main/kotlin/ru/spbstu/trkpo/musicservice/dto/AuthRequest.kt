package ru.spbstu.trkpo.musicservice.dto

import java.util.*

data class AuthRequest(val authCode: String, val userId: UUID)
