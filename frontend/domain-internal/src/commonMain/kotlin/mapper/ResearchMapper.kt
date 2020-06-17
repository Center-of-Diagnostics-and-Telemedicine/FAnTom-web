package mapper

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
    Event.Close -> null
  }
}

val researchEventToOutput: Event.() -> Output? = {
  when (this) {
    is Event.Close -> Output.Close
    is Event.Reload,
    is Event.DismissError,
    -> null
  }
}
