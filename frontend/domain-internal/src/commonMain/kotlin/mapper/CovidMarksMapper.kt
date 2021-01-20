package mapper

import controller.CovidMarksController.Input
import controller.CovidMarksController.Output
import store.covid.CovidMarksStore.*
import view.CovidMarksView.Event
import view.CovidMarksView.Model

val covidMarksStateToModel: State.() -> Model = {
  Model(loading = loading, lungLobeModels = covidLungLobes, error = error)
}

val covidMarksEventToIntent: Event.() -> Intent? = {
  when (this) {
    is Event.VariantChosen -> Intent.ChangeVariant(lungLobeModel, variant)
  }
}

val inputToCovidMarksIntent: Input.() -> Intent? = {
  when (this) {
    Input.Idle -> TODO()
    Input.CloseResearchRequested -> TODO()
  }
}

val covidMarksLabelToMarksOutput: Label.() -> Output? = {
  when (this) {
    Label.CloseResearch -> TODO()
  }
}