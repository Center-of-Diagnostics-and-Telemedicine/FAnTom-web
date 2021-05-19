package root

import DEBUG
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.browser.document
import kotlinx.browser.window
import local.LoginLocalDataSource
import local.ResearchLocalDataSource
import react.dom.render
import remote.*
import repository.*
import storeFactoryInstance

/**
Copyright (c) 2021, Moscow Center for Diagnostics & Telemedicine
All rights reserved.
This file is licensed under BSD-3-Clause license. See LICENSE file for details.
 */

private class Application {

  val authRepository = LoginRepositoryImpl(
    local = LoginLocalDataSource,
    remote = LoginRemoteDataSource
  )

  val getToken: suspend () -> String = { authRepository.local.getToken() }

  val researchesRepository = ResearchRepositoryImpl(
    local = ResearchLocalDataSource,
    remote = ResearchRemoteDataSource,
    token = getToken
  )

  val markRepository = MarksRepositoryImpl(
    remote = MarksRemoteDataSource,
    token = getToken
  )

  val expertMarkRepository = ExpertMarksRepositoryImpl(
    remote = ExpertMarksRemoteDataSource,
    token = getToken
  )

  val covidMarkRepository = CovidMarksRepositoryImpl(
    remote = CovidMarksRemoteDataSource,
    token = getToken
  )

  val brightnesRepository = BrightnessRepositoryImpl()
  val mippRepository = MipRepositoryImpl()

  fun start() {
    window.onload = {
      render(document.getElementById("app")) {
        app(dependencies = object : App.Dependencies {
          override val storeFactory: StoreFactory = storeFactoryInstance
          override val loginRepository: LoginRepository = authRepository
          override val researchRepository: ResearchRepository = researchesRepository
          override val marksRepository: MarksRepository = markRepository
          override val expertMarksRepository: ExpertMarksRepository = expertMarkRepository
          override val brightnessRepository: BrightnessRepository = brightnesRepository
          override val mipRepository: MipRepository = mippRepository
          override val covidMarksRepository: CovidMarksRepository = covidMarkRepository
        })
      }
    }
  }

}

fun main() {
  Application().start()
}

fun Any.debugLog(text: String?) {
  if (text.isNullOrEmpty().not() && DEBUG)
    console.log("${this::class.simpleName?.toUpperCase()}: $text")
}
