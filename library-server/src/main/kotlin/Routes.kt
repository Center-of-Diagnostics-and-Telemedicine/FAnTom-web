package com.marktomogram.backend

import com.marktomogram.*
import controller.ResearchController
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*
import model.*

fun Application.main(researchController: ResearchController) {

  processRequests(researchController)
}

fun Application.processRequests(researchController: ResearchController) {
  routing {

    get("hello") {
      call.respond("Hello world!")
    }

    post("{id}") {
      researchController.getSlice(call)
    }

    route(RESEARCH_ROUTE) {
      get("$INIT_ROUTE/{id}") {
        researchController.init(call)
      }
      get(LIST_ROUTE) {
        researchController.getAccessionNames(call)
      }
      get(HOUNSFIELD_ROUTE) {
        researchController.hounsfield(call)
      }
    }
  }
}
