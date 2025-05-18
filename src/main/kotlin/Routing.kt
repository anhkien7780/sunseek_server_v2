package com.server

import com.server.db.PostgresRepository
import com.server.model.Session
import com.server.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureRouting(postgresRepository: PostgresRepository) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {

        route("user") {
            post("register") {
                try {
                    val registerRequest = call.receive<User>()
                    if (postgresRepository.isUserExist(registerRequest.username)) {
                        call.respond(HttpStatusCode.Conflict, "Trùng lặp tài khoản")
                    } else {
                        postgresRepository.addUser(registerRequest)
                        call.respond(HttpStatusCode.Created)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    call.respond(status = HttpStatusCode.BadGateway, message = "Sự cố máy chủ")
                }
            }
            post("login") {
                try {
                    val loginRequest = call.receive<User>()
                    val user = postgresRepository.getUser(loginRequest.username) ?: return@post call.respond(
                        HttpStatusCode.NotFound, message = "Tài khoản không tồn tại"
                    )
                    if (loginRequest.password == user.password) {
                        val userID = postgresRepository.getUserID(user.username)
                        postgresRepository.addSession(userID)
                        val session = postgresRepository.getSession(userID)
                        call.sessions.set(session)
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    call.respond(status = HttpStatusCode.BadGateway, message = "Sự cố máy chủ")
                }
            }
            authenticate("auth-session") {
                post("logout") {
                    try {
                        val session = call.principal<Session>() ?: return@post call.respond(HttpStatusCode.Unauthorized)
                        if (postgresRepository.deleteSession(session = session)) {
                            call.respond(HttpStatusCode.OK, "Xóa phiên đăng nhập thành công")
                        } else {
                            call.respond(HttpStatusCode.Conflict, "Xóa phiên đăng nhập thất bại")
                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        call.respond(status = HttpStatusCode.BadGateway, message = "Sự cố máy chủ")
                    }
                }
            }
        }
    }
}