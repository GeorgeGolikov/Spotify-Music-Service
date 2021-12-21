package ru.spbstu.trkpo.musicservice.entity

import java.util.*
import javax.persistence.*

@Entity(name = "tokens_info")
@Table(name = "tokens_info")
class TokensInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id: Long? = null

    var userId: UUID? = null

    var accessToken: String? = null
    var refreshToken: String? = null
}