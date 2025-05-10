package com.server

import com.server.db.table.LocationTable
import com.server.db.table.SessionTable
import com.server.db.table.UserTable
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/sunseek_v2",
        user = "postgres",
        driver = "org.postgresql.Driver",
        password = "a12345",
    )
    transaction {
        SchemaUtils.create(
            UserTable, LocationTable, SessionTable
        )
    }
}
