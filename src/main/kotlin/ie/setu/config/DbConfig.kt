package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig{

    private val logger = KotlinLogging.logger {}

    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{

        logger.info{"Starting DB Connection..."}

        val PGUSER = "phkrnruk"
        val PGPASSWORD = "Hr9p4VRa0yaWM4SbSdxJwPNqEsiyRHz4"
        val PGHOST = "mel.db.elephantsql.com"
        val PGPORT = "5432"
        val PGDATABASE = "phkrnruk"

        //url format should be jdbc:postgresql://host:port/database
        val url = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        val dbConfig = Database.connect(
            url,
            driver = "org.postgresql.Driver",
            user = PGUSER,
            password = PGPASSWORD
        )

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig
    }

}