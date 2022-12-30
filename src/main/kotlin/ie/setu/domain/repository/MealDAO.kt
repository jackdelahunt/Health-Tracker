package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.Meal
import ie.setu.domain.db.Meals
import ie.setu.utils.mapToActivity
import ie.setu.utils.mapToMeal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class MealDAO {

    //Get all the meals in the database regardless of user id
    fun getAll(): ArrayList<Meal> {
        val mealList: ArrayList<Meal> = arrayListOf()
        transaction {
            Meals.selectAll().map {
                mealList.add(mapToMeal(it)) }
        }
        return mealList
    }

    //Find a specific meal by meal id
    fun findByMealId(id: Int): Meal? {
        return transaction { Meals
                .select() { Meals.id eq id}
                .map{ mapToMeal(it) }
                .firstOrNull()
        }
    }

    //Find all meals for a specific user id
    fun findByUserId(userId: Int): List<Meal>{
        return transaction {
            Meals
                .select {Meals.userId eq userId}
                .map { mapToMeal(it) }
        }
    }

    //Save a meal for the database
    fun save(meal: Meal): Int {
        return transaction {
            Meals.insert {
                it[description] = meal.description
                it[calories] = meal.calories
                it[fat] = meal.fat
                it[carbs] = meal.carbs
                it[userId] = meal.userId
            }
        } get Meals.id
    }

    fun updateByMealId(mealId: Int, mealToUpdate: Meal) : Int{
        return transaction {
            Meals.update ({
                Meals.id eq mealId}) {
                it[description] = mealToUpdate.description
                it[fat] = mealToUpdate.fat
                it[carbs] = mealToUpdate.carbs
                it[calories] = mealToUpdate.calories
                it[userId] = mealToUpdate.userId
            }
        }
    }

    fun deleteByMealId (mealId: Int): Int{
        return transaction{
            Meals.deleteWhere { Meals.id eq mealId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Meals.deleteWhere { Meals.userId eq userId }
        }
    }
}