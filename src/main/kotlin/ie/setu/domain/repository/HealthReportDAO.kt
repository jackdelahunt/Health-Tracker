package ie.setu.domain.repository

import ie.setu.domain.HealthReport
import ie.setu.domain.db.Activities
import ie.setu.domain.db.HealthReports
import ie.setu.utils.mapToHealthReport
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class HealthReportDAO {

    fun getAll(): ArrayList<HealthReport> {
        val healthReportList: ArrayList<HealthReport> = arrayListOf()
        transaction {
            HealthReports.selectAll().map {
                healthReportList.add(mapToHealthReport(it)) }
        }
        return healthReportList
    }

    fun findByHealthReportId(id: Int): HealthReport? {
        return transaction {
            HealthReports
                .select() { HealthReports.id eq id}
                .map{ mapToHealthReport(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<HealthReport>{
        return transaction {
            HealthReports
                .select { HealthReports.userId eq userId}
                .map { mapToHealthReport(it) }
        }
    }

    //Save an activity to the database
    fun save(healthReport: HealthReport): Int {
        return transaction {
            HealthReports.insert {
                it[weight] = healthReport.weight
                it[height] = healthReport.height
                it[BMI] = healthReport.BMI
                it[userId] = healthReport.userId
            }
        } get HealthReports.id
    }

    fun updateByHealthReportById(healthReportId: Int, healthReportToUpdate: HealthReport) : Int{
        return transaction {
            HealthReports.update ({
                HealthReports.id eq healthReportId}) {
                it[weight] = healthReportToUpdate.weight
                it[height] = healthReportToUpdate.height
                it[BMI] = healthReportToUpdate.BMI
                it[userId] = healthReportToUpdate.userId
            }
        }
    }

    fun deleteByHealthReportById (healthReportId: Int): Int{
        return transaction{
            HealthReports.deleteWhere { HealthReports.id eq healthReportId }
        }
    }
}