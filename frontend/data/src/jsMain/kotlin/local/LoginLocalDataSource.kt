package local

import model.TOKEN
import org.w3c.dom.get
import org.w3c.dom.set
import repository.LoginLocal
import kotlin.browser.localStorage

actual object LoginLocalDataSource : LoginLocal {

  override suspend fun getToken(): String {
    return localStorage[TOKEN] ?: ""
  }

  override suspend fun saveToken(token: String) {
    localStorage[TOKEN] = token
  }
}