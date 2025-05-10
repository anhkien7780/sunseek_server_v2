package com.server.db.table

import org.jetbrains.exposed.sql.Table

object LocationTable: Table("locations") {
    val locationID = integer("locationid").autoIncrement()
    val userID = reference("userid", UserTable.userID)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val name = varchar("name", 256)
    override val primaryKey = PrimaryKey(locationID, name = "locationid")
}