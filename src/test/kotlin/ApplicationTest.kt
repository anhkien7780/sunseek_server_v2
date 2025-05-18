package com.server

import com.server.model.User
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRegisterExistUser() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val response = client.post("user/register") {
            contentType(ContentType.Application.Json)
            setBody(User("admin@gmail.com", "admin"))
        }
        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun testSuccessLogin() = testApplication {

        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
        }
        val loginResponse = client.post("user/login") {
            contentType(ContentType.Application.Json)
            setBody(User("admin@gmail.com", "admin"))
        }
        assertEquals(HttpStatusCode.OK, loginResponse.status)
    }
    @Test
    fun testInvalidLogin() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
        }
        val loginResponse = client.post("user/login") {
            contentType(ContentType.Application.Json)
            setBody(User("wrong@gmail.com", "admin"))
        }
        assertEquals(HttpStatusCode.NotFound, loginResponse.status)
    }

    @Test
    fun testSuccessLogout() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
        }

        val loginResponse = client.post("user/login") {
            contentType(ContentType.Application.Json)
            setBody(User("admin@gmail.com", "admin"))
        }
        assertEquals(HttpStatusCode.OK, loginResponse.status)

        // Logout with a session
        val logoutResponse = client.post("user/logout")

        assertEquals(HttpStatusCode.OK, logoutResponse.status)
    }
}