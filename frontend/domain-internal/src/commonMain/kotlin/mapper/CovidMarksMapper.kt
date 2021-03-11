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
    Event.DismissError -> Intent.DismissError
  }
}

val inputToCovidMarksIntent: Input.() -> Intent? = {
  when (this) {
    Input.CloseResearchRequested -> Intent.HandleCloseResearch
    Input.Idle -> null
  }
}

val covidMarksLabelToMarksOutput: Label.() -> Output? = {
  when (this) {
    Label.CloseResearch -> Output.CloseResearch
  }
}