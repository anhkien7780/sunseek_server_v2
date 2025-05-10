package com.server.db.`interface`

import com.server.model.User

interface UserRepo {
    suspend fun addUser(user: User)
    suspend fun getUser(username: String): User?
    suspend fun isUserExist(username: String): Boolean
    suspend fun getUserID(username: String): Int
    suspend fun changeUserPassword(username: String, newPassword: String)
    suspend fun deleteUser(username: String): Boolean
}