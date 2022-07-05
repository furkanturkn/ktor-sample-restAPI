package com.furkan.plugins

import com.furkan.routes.employeeRoutes
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        employeeRoutes()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
