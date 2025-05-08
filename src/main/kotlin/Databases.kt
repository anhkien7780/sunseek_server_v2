package com.server

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    Database.connect(
        url = "jdbc:postgres://localhost:5432/sunseek",
        user = "postgres",
        driver = "org.postgresql.Driver",
        password = "a1234",
    )
}
