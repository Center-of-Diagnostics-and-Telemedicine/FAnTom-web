package data

import client.datasource.local.LocalDataSource
import model.TOKEN
import org.w3c.dom.set
import kotlin.browser.localStorage


class JsLocalDataSource : LocalDataSource {

  override fun saveToken(token: String) {
    localStorage[TOKEN] = token
  }

  override fun getToken(): String = localStorage.getItem(TOKEN) ?: ""

}