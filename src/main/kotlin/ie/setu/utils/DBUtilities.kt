package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.HealthReport
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.HealthReports
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToHealthReport(it: ResultRow) = HealthReport(
    id = it[HealthReports.id],
    weight = it[HealthReports.weight],
    height = it[HealthReports.height],
    BMI = it[HealthReports.BMI],
    userId = it[HealthReports.userId],
)