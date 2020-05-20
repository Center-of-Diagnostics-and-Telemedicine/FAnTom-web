package root

import com.arkivanov.mvikotlin.core.store.StoreFactory
import react.dom.render
import repository.LoginRepository
import repository.LoginRepositoryImpl
import storeFactoryInstance
import kotlin.browser.document
import kotlin.browser.window

private class Application {

    fun start() {
        window.onload = {
            render(document.getElementById("app")) {
                app(dependencies = object : App.Dependencies {
                    override val storeFactory: StoreFactory = storeFactoryInstance
                    override val loginRepository: LoginRepository = LoginRepositoryImpl()
                })
            }
        }
    }

}

fun main() {
    Application().start()
}

fun Any.debugLog(text: String?) {
    if (text.isNullOrEmpty().not()) console.log("${this::class.simpleName?.toUpperCase()}: $text")
}