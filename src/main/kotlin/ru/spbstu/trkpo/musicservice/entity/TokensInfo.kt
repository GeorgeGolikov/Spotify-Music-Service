package ru.spbstu.trkpo.musicservice.entity

import java.util.*
import javax.persistence.*

@Entity(name = "tokens_info")
@Table(name = "tokens_info")
class TokensInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id: Long? = null

    private var userId: UUID? = null

    private var accessToken: String? = null
    private var refreshToken: String? = null
}