package ie.setu.repository

import ie.setu.domain.HealthReport
import ie.setu.domain.db.Activities
import ie.setu.domain.repository.HealthReportDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

//retrieving some test data from Fixtures
private val healthReport1 = healthReports.get(0)
private val healthReport2 = healthReports.get(1)
private val healthReport3 = healthReports.get(2)


class HealthReportDAOTest {
    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateActivities {

        @Test
        fun `multiple health reports added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()
                //Act & Assert
                Assertions.assertEquals(3, healthReportDAO.getAll().size)
                Assertions.assertEquals(healthReport1, healthReportDAO.findByHealthReportId(healthReport1.id))
                Assertions.assertEquals(healthReport2, healthReportDAO.findByHealthReportId(healthReport2.id))
                Assertions.assertEquals(healthReport3, healthReportDAO.findByHealthReportId(healthReport3.id))
            }
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()
                //Act & Assert
                Assertions.assertEquals(3, healthReportDAO.getAll().size)
            }
        }

        @Test
        fun `get healthReport by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()
                //Act & Assert
                Assertions.assertEquals(1, healthReportDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get healthReport by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()
                //Act & Assert
                Assertions.assertEquals(healthReport1, healthReportDAO.findByUserId(1).get(0))
            }
        }

        @Test
        fun `get healthReport by healthReport id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()
                //Act & Assert
                Assertions.assertEquals(null, healthReportDAO.findByHealthReportId(4))
            }
        }

        @Test
        fun `get healthReport by healthReport id that exists, results in a correct healthReport returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()
                //Act & Assert
                Assertions.assertEquals(healthReport1, healthReportDAO.findByHealthReportId(1))
                Assertions.assertEquals(healthReport3, healthReportDAO.findByHealthReportId(3))
            }
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating existing healthReport in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()

                //Act & Assert
                val healthReport3updated = HealthReport(id = 3, weight = 100, height = 1.88, BMI = 230, userId = 2)
                healthReportDAO.updateByHealthReportById(healthReport3updated.id, healthReport3updated)
                Assertions.assertEquals(healthReport3updated, healthReportDAO.findByHealthReportId(3))
            }
        }

        @Test
        fun `updating non-existant healthReport in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()

                //Act & Assert
                val healthReport3updated = HealthReport(id = 3, weight = 100, height = 1.88, BMI = 230, userId = 2)
                healthReportDAO.updateByHealthReportById(4, healthReport3updated)
                Assertions.assertEquals(null, healthReportDAO.findByHealthReportId(4))
                Assertions.assertEquals(3, healthReportDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting a non-existant healthReport (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()

                //Act & Assert
                Assertions.assertEquals(3, healthReportDAO.getAll().size)
                healthReportDAO.deleteByHealthReportById(4)
                Assertions.assertEquals(3, healthReportDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing healthReport (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val healthReportDAO = populateHealthReportTable()

                //Act & Assert
                Assertions.assertEquals(3, healthReportDAO.getAll().size)
                healthReportDAO.deleteByHealthReportById(healthReport3.id)
                Assertions.assertEquals(2, healthReportDAO.getAll().size)
            }
        }
    }
}