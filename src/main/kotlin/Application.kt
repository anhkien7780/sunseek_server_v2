package com.server

import com.server.db.PostgresRepository
import com.server.model.Session
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respond
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.sameSite

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(testing: Boolean = true) {
    val postgresRepository = PostgresRepository()
    install(Sessions) {
        cookie<Session>("session_cookie") {
            cookie.sameSite = "Lax"
            cookie.secure = testing.not()
            cookie.httpOnly = true
        }
    }
    install(Authentication) {
        session<Session>("auth-session") {
            validate { session ->
                println("Validate session: $session")
                if (postgresRepository.isSessionExist(session.sessionID)) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = "Phiên đăng nhập hết hạn hoặc không tồn tại"
                )
            }
        }
    }

    configureSerialization()
    configureDatabases()
    configureRouting(postgresRepository)
}
