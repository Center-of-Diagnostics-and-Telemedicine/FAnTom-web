package mapper

import controller.MarksController.Input
import controller.MarksController.Output
import store.marks.MarksStore.*
import view.MarksView.Event
import view.MarksView.Model

val marksStateToModel: State.() -> Model = {
  Model(items = marks, current = current, markTypes = markTypes, error = error)
}

val marksEventToIntent: Event.() -> Intent? = {
  when (this) {
    is Event.SelectItem -> Intent.SetCurrentMark(mark)
    is Event.ItemCommentChanged -> Intent.UpdateComment(mark, comment)
    is Event.DeleteItem -> Intent.DeleteMark(mark)
    is Event.ChangeMarkType -> Intent.ChangeMarkType(type, markId)
    is Event.ChangeVisibility -> Intent.ChangeVisibility(mark)
    Event.DissmissError -> Intent.DismissError
  }
}

val inputToMarksIntent: Input.() -> Intent? = {
  when (this) {
    is Input.AddNewMark -> Intent.HandleNewMark(shape, sliceNumber, cut)
    is Input.SelectMark -> Intent.SelectMark(mark)
    is Input.UnselectMark -> Intent.UnselectMark(mark)
    is Input.UpdateMarkWithoutSave -> Intent.UpdateMarkWithoutSaving(markToUpdate)
    is Input.UpdateMarkWithSave -> Intent.UpdateMarkWithSave(mark)
    Input.DeleteClick -> Intent.DeleteClicked
    Input.CloseResearchRequested -> Intent.HandleCloseResearch
    Input.Idle -> null
  }
}

val marksLabelToMarksOutput: Label.() -> Output? = {
  when (this) {
    is Label.MarksLoaded -> Output.Marks(list)
    Label.CloseResearch -> Output.CloseResearch
    is Label.CenterSelectedMark -> Output.CenterSelectedMark(mark)
  }
}
