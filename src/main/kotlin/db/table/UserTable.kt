package com.server.db.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table("users") {
    val userID = integer("userid").autoIncrement()
    val username = varchar("username", 256)
    val password = varchar("password", 256)
    override val primaryKey = PrimaryKey(userID, name = "userid")
}