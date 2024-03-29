package ie.setu.helpers


import ie.setu.domain.Activity
import ie.setu.domain.HealthReport
import ie.setu.domain.Meal
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.HealthReports
import ie.setu.domain.db.Meals
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.HealthReportDAO
import ie.setu.domain.repository.MealDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime


val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 945
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)

val healthReports = arrayListOf<HealthReport>(
    HealthReport(id = 1, 67, height = 1.87, BMI = 12, 1),
    HealthReport(id = 2, 77, height = 1.90, BMI = 11, 2),
    HealthReport(id = 3, 80, height = 1.67, BMI = 16, 3),
    HealthReport(id = 4, 88, height = 1.75, BMI = 14, 4),
)

val meals = arrayListOf<Meal>(
    Meal(id = 1, "High protein curry with chicken", calories = 600, fat = 12, carbs = 50, 1),
    Meal(id = 2, "High protein fried chicken low fat sour sauce", calories = 400, fat = 20, carbs = 40, 2),
)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}

fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}

fun populateHealthReportTable(): HealthReportDAO {
    SchemaUtils.create(HealthReports)
    val healthReportDAO = HealthReportDAO()
    healthReportDAO.save(healthReports[0])
    healthReportDAO.save(healthReports[1])
    healthReportDAO.save(healthReports[2])
    return healthReportDAO
}

fun populateMealTable(): MealDAO {
    SchemaUtils.create(Meals)
    val mealDAO = MealDAO()
    mealDAO.save(meals[0])
    mealDAO.save(meals[1])
    return mealDAO
}
