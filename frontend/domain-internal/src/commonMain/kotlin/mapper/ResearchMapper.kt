package mapper

import controller.ResearchController.Output
import store.research.ResearchStore.*
import view.ResearchView.Event
import view.ResearchView.Model

val researchStateToResearchModel: State.() -> Model? = {
  Model(
    data = data,
    error = error,
    loading = loading
  )
}

val researchEventToResearchIntent: Event.() -> Intent? = {
  when (this) {
    Event.DismissError -> Intent.DismissError
    Event.Reload -> Intent.ReloadRequested
    Event.Close -> Intent.CloseRequested
  }
}

val researchLabelToResearchOutput: Label.() -> Output? = {
  when (this) {
    Label.Close -> Output.Close
  }
}
