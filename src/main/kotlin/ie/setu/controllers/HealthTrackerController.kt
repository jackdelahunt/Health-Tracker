package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.User
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context

object HealthTrackerController {

    private val userDAO = UserDAO()

    fun getAllUser(ctx: Context) {
        println("GET all users")
        ctx.json(userDAO.getAll())
    }

    fun getUserByUserId(ctx: Context) {
        println("GET user by id")

        val user = userDAO.findById(ctx.pathParam("user-id").toInt())

        if(user != null) {
            ctx.json(user)
        }
    }

    fun addUser(ctx: Context) {
        println("POST create user")

        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDAO.save(user)
        ctx.json(user)
    }

    fun getUserByEmail(ctx: Context) {
        println("GET user by email")

        val user = userDAO.findByEmail(ctx.pathParam("email").toString())

        if(user != null) {
            ctx.json(user)
        }
    }

    fun deleteUser(ctx: Context){
        println("DELETE user by id")

        userDAO.delete(ctx.pathParam("user-id").toInt())
    }

    fun updateUser(ctx: Context){
        println("PATCH user by id")

        val id = ctx.pathParam("user-id").toInt()
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDAO.updateUser(id, user)
    }

}