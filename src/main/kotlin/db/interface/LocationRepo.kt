package com.server.db.`interface`

import com.server.model.Location
import com.server.model.LocationWithID

interface LocationRepo {
    suspend fun addLocation(userID: Int, location: Location)
    suspend fun getListLocation(userID: Int): List<LocationWithID>
    suspend fun deleteLocation(locationID: Int): Boolean
}