package com.furkan.data

import com.furkan.data.model.Employee
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("CompanyDatabase")

private val employees = database.getCollection<Employee>()

suspend fun getEmployeeForId(id: String): Employee? {
    return employees.findOneById(id)
}

suspend fun createEmployeeOrUpdateEmployeeForId(employee: Employee): Boolean {
    val employeeExists = employees.findOneById(employee.id) != null
    return if (employeeExists) {
        employees.updateOneById(employee.id, employee).wasAcknowledged()
    } else {
        employee.id = ObjectId().toString()
        employees.insertOne(employee).wasAcknowledged()
    }
}

suspend fun deleteEmployeeForId(employeeId: String): Boolean {
    val employee = employees.findOne(Employee::id eq employeeId)
    employee?.let { employee ->
        return employees.deleteOneById(employee.id).wasAcknowledged()
    } ?: return false

}

