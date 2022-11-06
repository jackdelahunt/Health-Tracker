package ie.setu.domain.db


import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object HealthReports : Table("healthreports") {
    val id = integer("id").autoIncrement().primaryKey()
    val weight = integer("weight")
    val height = double("height")
    val BMI = integer("BMI")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}