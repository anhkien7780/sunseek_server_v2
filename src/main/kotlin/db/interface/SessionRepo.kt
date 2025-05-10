package com.server.db.`interface`

import com.server.model.Session

interface SessionRepo {
    suspend fun addSession(userID: Int)
    suspend fun isSessionExist(sessionID: String): Boolean
    suspend fun getSession(userID: Int): Session
    suspend fun deleteSession(session: Session): Boolean
}