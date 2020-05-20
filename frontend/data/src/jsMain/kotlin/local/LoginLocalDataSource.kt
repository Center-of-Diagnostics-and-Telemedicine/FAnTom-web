package local

import model.TOKEN
import org.w3c.dom.set
import kotlin.browser.localStorage

actual class LoginLocalDataSource : LoginLocal {
    override fun getToken() {

    }

    override fun saveToken(token: String) {
        localStorage[TOKEN] = token
    }
}