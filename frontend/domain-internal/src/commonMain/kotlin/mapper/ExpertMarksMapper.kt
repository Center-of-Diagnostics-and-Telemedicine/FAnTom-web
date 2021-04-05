package mapper

import controller.ExpertMarksController
import store.expert.ExpertMarksStore
import view.ExpertMarksView

val expertMarksStateToModel: ExpertMarksStore.State.() -> ExpertMarksView.Model = {
  ExpertMarksView.Model(
    loading = loading,
    error = error,
    models = models
  )
}

val expertMarksEventToIntent: ExpertMarksView.Event.() -> ExpertMarksStore.Intent? = {
  when (this) {
    is ExpertMarksView.Event.VariantChosen ->
      ExpertMarksStore.Intent.ChangeVariant(expertMarkEntity, question)
    is ExpertMarksView.Event.SelectMark -> ExpertMarksStore.Intent.SelectMark(id)
    ExpertMarksView.Event.DismissError -> ExpertMarksStore.Intent.DismissError
  }
}

val inputToExpertMarksIntent: ExpertMarksController.Input.() -> ExpertMarksStore.Intent? = {
  when (this) {
    is ExpertMarksController.Input.AcceptMark -> ExpertMarksStore.Intent.AcceptMark(mark)
    is ExpertMarksController.Input.SelectMark -> ExpertMarksStore.Intent.SelectMark(mark.id)
    ExpertMarksController.Input.CloseResearchRequested -> ExpertMarksStore.Intent.HandleCloseResearch
    ExpertMarksController.Input.Idle -> null
  }
}

val expertMarksLabelToMarksOutput: ExpertMarksStore.Label.() -> ExpertMarksController.Output? = {
  when (this) {
    ExpertMarksStore.Label.CloseResearch -> ExpertMarksController.Output.CloseResearch
    is ExpertMarksStore.Label.Marks -> ExpertMarksController.Output.Marks(models)
  }
}