package mapper

import controller.ExpertMarksController
import store.expert.ExpertMarksStore
import view.ExpertMarksView

val expertMarksStateToModel: ExpertMarksStore.State.() -> ExpertMarksView.Model = {
  ExpertMarksView.Model(
    loading = loading,
    error = error,
    rois = roisQuestions
  )
}

val expertMarksEventToIntent: ExpertMarksView.Event.() -> ExpertMarksStore.Intent? = {
  when (this) {
    is ExpertMarksView.Event.VariantChosen<*> ->
      ExpertMarksStore.Intent.ChangeVariant(question, variant)
    ExpertMarksView.Event.DismissError -> ExpertMarksStore.Intent.DismissError
  }
}

val inputToExpertMarksIntent: ExpertMarksController.Input.() -> ExpertMarksStore.Intent? = {
  when (this) {
    ExpertMarksController.Input.CloseResearchRequested -> ExpertMarksStore.Intent.HandleCloseResearch
    ExpertMarksController.Input.Idle -> null
  }
}

val expertMarksLabelToMarksOutput: ExpertMarksStore.Label.() -> ExpertMarksController.Output? = {
  when (this) {
    ExpertMarksStore.Label.CloseResearch -> ExpertMarksController.Output.CloseResearch
  }
}