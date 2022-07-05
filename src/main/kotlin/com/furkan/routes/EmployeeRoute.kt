package com.furkan.routes

import com.furkan.data.SimpleResponse
import com.furkan.data.createEmployeeOrUpdateEmployeeForId
import com.furkan.data.deleteEmployeeForId
import com.furkan.data.getEmployeeForId
import com.furkan.data.model.Employee
import com.furkan.data.requests.DeleteEmployeeRequest
import com.furkan.data.requests.EmployeeRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.employeeRoutes() {
    route("/get-employee") {
        get {
            val employeeId = call.receive<EmployeeRequest>().id
            val employee = getEmployeeForId(employeeId)
            employee?.let {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(true, "Employee successfully retrieved", it)
                )
            } ?: call.respond(
                HttpStatusCode.OK,
                SimpleResponse(true, "There is no employee with this id", Unit)

            )

        }
    }

    route("/create-update-employee") {
        post {
            val request = try {
                call.receive<Employee>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (createEmployeeOrUpdateEmployeeForId(request)) {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(true, "Employee successfully created/ updated", Unit)

                )
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }
    }

    route("/delete-employee") {
        post {
            val request = try {
                call.receive<DeleteEmployeeRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (deleteEmployeeForId(request.id)) {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(true, "Employee successfully deleted", Unit)
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    SimpleResponse(true, "Employee not found", Unit)
                )
            }
        }
    }


}