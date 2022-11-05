package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{

        logger.info{"Starting DB Connection..."}

        val dbConfig = Database.connect(
            "jdbc:postgresql://ec2-18-215-41-121.compute-1.amazonaws.com:5432/d9bv0katptoa6k?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "cnbpybtwwfwpol",
            password = "0247af8d2320f76667a5a4947e96bc608932c56996325d57cbf4558ff6e5e87f")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}