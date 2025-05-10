package com.server.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationWithID(
    val id: Int,
    val userID: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
)