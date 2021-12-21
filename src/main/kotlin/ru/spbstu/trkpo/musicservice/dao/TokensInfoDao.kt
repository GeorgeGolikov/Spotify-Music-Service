package ru.spbstu.trkpo.musicservice.dao

import org.springframework.data.jpa.repository.JpaRepository
import ru.spbstu.trkpo.musicservice.entity.TokensInfo
import java.util.*

interface TokensInfoDao: JpaRepository<TokensInfo, Long> {
    fun findByUserId(userId: UUID): TokensInfo?
}