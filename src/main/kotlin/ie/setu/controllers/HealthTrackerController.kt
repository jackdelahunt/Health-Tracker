package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.User
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.Activity
import ie.setu.domain.HealthReport
import ie.setu.domain.repository.HealthReportDAO
import ie.setu.utils.jsonToObject


object HealthTrackerController {

    private val userDAO = UserDAO()
    private val activityDAO = ActivityDAO()
    private val healthReportDAO = HealthReportDAO()

    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<User>::class)])]
    )
    fun getAllUsers(ctx: Context) {
        val users = userDAO.getAll()
        if (users.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(users)
    }

    @OpenApi(
        summary = "Get user by ID",
        operationId = "getUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(User::class)])]
    )
    fun getUserByUserId(ctx: Context) {
        val user = userDAO.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add User",
        operationId = "addUser",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun addUser(ctx: Context) {
        val user : User = jsonToObject(ctx.body())
        val userId = userDAO.save(user)
        if (userId != null) {
            user.id = userId
            ctx.json(user)
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Get user by Email",
        operationId = "getUserByEmail",
        tags = ["User"],
        path = "/api/users/email/{email}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("email", Int::class, "The user email")],
        responses  = [OpenApiResponse("200", [OpenApiContent(User::class)])]
    )
    fun getUserByEmail(ctx: Context) {
        val user = userDAO.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete user by ID",
        operationId = "deleteUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteUser(ctx: Context){
        if (userDAO.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update user by ID",
        operationId = "updateUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateUser(ctx: Context){
        val foundUser : User = jsonToObject(ctx.body())
        if ((userDAO.update(id = ctx.pathParam("user-id").toInt(), user=foundUser)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Get activities by user id",
        operationId = "getActivitiesByUserId",
        tags = ["User"],
        path = "/api/users/{user-id}/activities",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user id")],
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Activity>::class)])]

    )
    fun getActivitiesByUserId(ctx: Context) {
        if (userDAO.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                ctx.json(activities)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete activities by user id",
        operationId = "deleteActivitiesByUserId",
        tags = ["User"],
        path = "/api/users/{user-id}/activities",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user id")],
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Activity>::class)])]

    )
    fun deleteActivityByUserId(ctx: Context){
        if (activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    /*
            ACTIVITIES
     */
    @OpenApi(
        summary = "Get all activities",
        operationId = "getAllActivities",
        tags = ["Activity"],
        path = "/api/activities",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Activity>::class)])]

    )
    fun getAllActivities(ctx: Context) {
        val activities = activityDAO.getAll()
        ctx.status(200)
        ctx.json(activities)
    }

    @OpenApi(
        summary = "Get activity by activity id",
        operationId = "getActivityByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.GET,
        responses  = [OpenApiResponse("200", [OpenApiContent(Activity::class)])]
    )
    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null){
            ctx.json(activity)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add Activity",
        operationId = "addActivity",
        tags = ["Activity"],
        path = "/api/activities",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addActivity(ctx: Context) {
        val activity : Activity = jsonToObject(ctx.body())
        val userId = userDAO.findById(activity.userId)
        if (userId != null) {
            val activityId = activityDAO.save(activity)
            activity.id = activityId
            ctx.json(activity)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete activity by activityID",
        operationId = "deleteActivityByActivityId",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun deleteActivityByActivityId(ctx: Context){
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update activity by ID",
        operationId = "updateActivityById",
        tags = ["Activity"],
        path = "/api/activities/{activity-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("activity-id", Int::class, "The activity ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun updateActivity(ctx: Context){
        val activity : Activity = jsonToObject(ctx.body())
        if (activityDAO.updateByActivityId(
                activityId = ctx.pathParam("activity-id").toInt(),
                activityToUpdate =activity) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    /*
           HEALTH REPORT
    */
    @OpenApi(
        summary = "Get all health reports",
        operationId = "getAllHealthReports",
        tags = ["HealthReport"],
        path = "/api/health-reports",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<HealthReport>::class)])]

    )
    fun getAllHealthReports(ctx: Context) {
        val healthReports = healthReportDAO.getAll()
        if (healthReports.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(healthReports)
    }

    @OpenApi(
        summary = "Get health report by health report id",
        operationId = "getHealthReportByHealthReportId",
        tags = ["HealthReport"],
        path = "/api/health-reports/{health-report-id}",
        method = HttpMethod.GET,
        responses  = [OpenApiResponse("200", [OpenApiContent(Activity::class)])]
    )
    fun getHealthReportsByHealthReportsId(ctx: Context) {
        val healthReport = healthReportDAO.findByHealthReportId((ctx.pathParam("health-report-id").toInt()))
        if (healthReport != null){
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(healthReport))
        }
    }

    @OpenApi(
        summary = "Add Health Report",
        operationId = "addHealthReport",
        tags = ["HealthReport"],
        path = "/api/health-reports",
        method = HttpMethod.POST,
        responses  = [OpenApiResponse("200")]
    )
    fun addHealthReport(ctx: Context) {
        val healthReport : HealthReport = jsonToObject(ctx.body())
        val userId = userDAO.findById(healthReport.userId)
        if (userId != null) {
            val healthReportId = healthReportDAO.save(healthReport)
            healthReport.id = healthReportId
            ctx.json(healthReport)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete health report by health reportId",
        operationId = "deleteHealthReportByHealthReportId",
        tags = ["HealthReport"],
        path = "/api/health-report/{health-report-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("health-report-id", Int::class, "The health report ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun deleteHealthReportByHealthReportId(ctx: Context){
        if (healthReportDAO.deleteByHealthReportById(ctx.pathParam("health-report-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update health report by ID",
        operationId = "updateHealthReportById",
        tags = ["HealthReport"],
        path = "/api/health-report/{health-report-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("health-report-id", Int::class, "The health report ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun updateHealReport(ctx: Context){
        val healthReport : HealthReport = jsonToObject(ctx.body())
        if (healthReportDAO.updateByHealthReportById(
                healthReportId = ctx.pathParam("health-report-id").toInt(),
                healthReportToUpdate =healthReport) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}