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
import ie.setu.domain.Meal
import ie.setu.domain.repository.ActivityDAO
import ie.setu.helpers.*
import kotlin.test.assertEquals

//retrieving some test data from Fixtures
private val meal1 = meals.get(0)
private val meal2 = meals.get(1)

class MealDAOTestDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateMeals {

        @Test
        fun `multiple meals added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(2, mealDAO.getAll().size)
                assertEquals(meal1, mealDAO.findByMealId(meal1.id))
                assertEquals(meal2, mealDAO.findByMealId(meal2.id))
            }
        }

        @Test
        fun `getting all meals from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(2, mealDAO.getAll().size)
            }
        }

        @Test
        fun `updating existing meal in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                val mealUpdaed = Meal(id = 2,
                    description = "Low fat beef",
                    calories = 800,
                    fat = 30,
                    carbs = 60,
                    userId = 2)

                mealDAO.updateByMealId(mealUpdaed.id, mealUpdaed)
                assertEquals(mealUpdaed, mealDAO.findByMealId(2))
            }
        }
    }
}