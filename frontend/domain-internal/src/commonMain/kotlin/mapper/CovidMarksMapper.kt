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
//    is MarksView.Event.SelectItem -> MarksStore.Intent.SetCurrentMark(mark)
//    is MarksView.Event.ItemCommentChanged -> MarksStore.Intent.UpdateComment(mark, comment)
//    is MarksView.Event.DeleteItem -> MarksStore.Intent.DeleteMark(mark)
//    is MarksView.Event.ChangeMarkType -> MarksStore.Intent.ChangeMarkType(type, markId)
//    MarksView.Event.DissmissError -> MarksStore.Intent.DismissError
    is Event.VariantChosen -> Intent.ChangeVariant(lungLobeModel, variant)
  }
}

val inputToCovidMarksIntent: Input.() -> Intent? = {
  when (this) {
//   Input.AddNewMark -> MarksStore.Intent.HandleNewMark(circle, sliceNumber, cut)
//   Input.SelectMark -> MarksStore.Intent.SelectMark(mark)
//   Input.UnselectMark -> MarksStore.Intent.UnselectMark(mark)
//   Input.UpdateMarkWithoutSave -> MarksStore.Intent.UpdateMarkWithoutSaving(markToUpdate)
//   Input.UpdateMarkWithSave -> MarksStore.Intent.UpdateMarkWithSave(mark)
//   Input.DeleteClick -> MarksStore.Intent.DeleteClicked
//   Input.CloseResearchRequested -> MarksStore.Intent.HandleCloseResearch
//   Input.Idle -> null
    Input.Idle -> TODO()
    Input.CloseResearchRequested -> TODO()
  }
}

val covidMarksLabelToMarksOutput: Label.() -> Output? = {
  when (this) {
//    is MarksStore.Label.MarksLoaded -> Output.Marks(list)
//    MarksStore.Label.CloseResearch -> Output.CloseResearch
    Label.CloseResearch -> TODO()
  }
}