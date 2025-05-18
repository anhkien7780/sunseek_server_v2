package com.server.db.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SessionTable: Table("sessions") {
    val sessionID = uuid("sessionid")
    val userID = reference("userid", UserTable.userID)
    val timeCreated = timestamp("timecreated")
    val timeExpired = timestamp("timeexpired")
    override val primaryKey = PrimaryKey(sessionID)
}