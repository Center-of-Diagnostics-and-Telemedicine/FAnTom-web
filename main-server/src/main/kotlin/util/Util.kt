package util

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File


fun Any.debugLog(text: String) {
  println("${this.javaClass.simpleName.toUpperCase()}: $text")
}

fun <T> database(statement: Transaction.() -> T): T {
  Database.connect(
    url = "jdbc:mysql://localhost:3306/mark_tomogram?characterEncoding=utf8&useUnicode=true",
    driver = "com.mysql.jdbc.Driver",
    user = "root",
    password = ""
  )

  return transaction {
    statement()
  }
}

private val userIdPattern = "[a-zA-Z0-9_\\.]+".toRegex()

internal fun userNameValid(userName: String) = userName.matches(userIdPattern)