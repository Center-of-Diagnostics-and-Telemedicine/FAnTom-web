package local

import kotlinx.browser.localStorage
import model.TOKEN
import org.w3c.dom.get
import org.w3c.dom.set
import repository.LoginLocal

actual object LoginLocalDataSource : LoginLocal {

  override suspend fun getToken(): String {
    return localStorage[TOKEN] ?: ""
  }

  override suspend fun saveToken(token: String) {
    localStorage[TOKEN] = token
  }
}