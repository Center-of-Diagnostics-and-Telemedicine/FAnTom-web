package root

import com.arkivanov.mvikotlin.core.store.StoreFactory
import local.LoginLocalDataSource
import local.MarksLocalDataSource
import local.ResearchLocalDataSource
import react.dom.render
import remote.LoginRemoteDataSource
import remote.MarksRemoteDataSource
import remote.ResearchRemoteDataSource
import repository.*
import storeFactoryInstance
import kotlin.browser.document
import kotlin.browser.window

private class Application {

  val authRepository = LoginRepositoryImpl(
    local = LoginLocalDataSource,
    remote = LoginRemoteDataSource
  )
  val researchesRepository = ResearchRepositoryImpl(
    local = ResearchLocalDataSource,
    remote = ResearchRemoteDataSource,
    token = { authRepository.local.getToken() }
  )

  val markRepository = MarksRepositoryImpl(
    local = MarksLocalDataSource,
    remote = MarksRemoteDataSource,
    token = { authRepository.local.getToken() }
  )

  fun start() {
    window.onload = {
      render(document.getElementById("app")) {
        app(dependencies = object : App.Dependencies {
          override val storeFactory: StoreFactory = storeFactoryInstance
          override val loginRepository: LoginRepository = authRepository
          override val researchRepository: ResearchRepository = researchesRepository
          override val marksRepository: MarksRepository = markRepository
        })
      }
    }
  }

}

fun main() {
  Application().start()
}

fun Any.debugLog(text: String?) {
  if (text.isNullOrEmpty().not())
    console.log("${this::class.simpleName?.toUpperCase()}: $text")
}
