package com.server.model

import kotlinx.serialization.Serializable

@Serializable
data class Session (
    val sessionID: String,
    val userID: Int,
    val timeCreated: String,
    val timeExpired: String
)
