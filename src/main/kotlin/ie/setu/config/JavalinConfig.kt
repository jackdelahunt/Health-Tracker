package ie.setu.config

import ie.setu.controllers.HealthTrackerController
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.rendering.vue.VueComponent
import io.swagger.v3.oas.models.info.Info

class JavalinConfig {
    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            it.registerPlugin(getConfiguredOpenApiPlugin())
            it.defaultContentType = "application/json"
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.enableWebjars()
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)

        return app
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {

            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))

            get("/activities", VueComponent("<activity-overview></activity-overview>"))

            get("/health-reports", VueComponent("<health-reports-overview></health-reports-overview>"))

            path("/api/users") {

                get(HealthTrackerController::getAllUsers)
                post(HealthTrackerController::addUser)

                path("{user-id}") {
                    get(HealthTrackerController::getUserByUserId)
                    delete(HealthTrackerController::deleteUser)
                    patch(HealthTrackerController::updateUser)

                    path("activities"){
                        get(HealthTrackerController::getActivitiesByUserId)
                        delete(HealthTrackerController::deleteActivityByUserId)
                    }
                }

                path("email/{email}") {
                    get(HealthTrackerController::getUserByEmail)
                }
            }

            path("/api/activities") {
                get(HealthTrackerController::getAllActivities)
                post(HealthTrackerController::addActivity)
                path("{activity-id}") {
                    get(HealthTrackerController::getActivitiesByActivityId)
                    delete(HealthTrackerController::deleteActivityByActivityId)
                    patch(HealthTrackerController::updateActivity)
                }
            }

            path("/api/meals") {
                get(HealthTrackerController::getAllMeals)
                post(HealthTrackerController::addMeal)
                path("{meal-id}") {
                    get(HealthTrackerController::getMealsByMealId)
                    delete(HealthTrackerController::deleteMealByMealId)
                    patch(HealthTrackerController::updateMeal)
                }
            }

            path("/api/health-reports") {
                get(HealthTrackerController::getAllHealthReports)
                post(HealthTrackerController::addHealthReport)
                path("{health-report-id}") {
                    get(HealthTrackerController::getHealthReportsByHealthReportsId)
                    delete(HealthTrackerController::deleteHealthReportByHealthReportId)
                    patch(HealthTrackerController::updateHealReport)
                }
            }
        }
    }

    private fun getRemoteAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 8080
    }

    fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title("Health Tracker App")
                version("1.0")
                description("Health Tracker API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            reDoc(ReDocOptions("/redoc")) // endpoint for redoc
        }
    )
}