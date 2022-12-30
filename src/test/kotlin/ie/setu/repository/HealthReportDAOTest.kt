package ie.setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ie.setu.domain.db.Activities
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val healthReport1 = healthReports.get(0)
private val healthReport2 = healthReports.get(1)
private val healthReport3 = healthReports.get(2)
private val healthReport4 = healthReports.get(3)

class HealthReportDAOTestDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateHealthReports {

        @Test
        fun `multiple health reports added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()

                //Act & Assert
                assertEquals(3, healthReportDAO.getAll().size)
                assertEquals(healthReport1, healthReportDAO.findByHealthReportId(healthReport1.id))
                assertEquals(healthReport2, healthReportDAO.findByHealthReportId(healthReport2.id))
                assertEquals(healthReport3, healthReportDAO.findByHealthReportId(healthReport3.id))
            }
        }
    }
}