

import org.flywaydb.core.Flyway

object DBMigration {

  fun migrate() {
    val url = "jdbc:mysql://localhost:3306/mark_tomogram"
    val user = "root"
    val password = ""
    val flyway = Flyway.configure().dataSource(url, user, password).locations("db").load()
    flyway.migrate()
  }
}