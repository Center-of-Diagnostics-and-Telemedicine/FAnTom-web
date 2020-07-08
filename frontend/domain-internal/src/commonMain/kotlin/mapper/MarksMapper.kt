package mapper

import controller.MarksController.Input
import controller.MarksController.Output
import store.marks.MarksStore.*
import view.MarksView.Event
import view.MarksView.Model

val marksStateToModel: State.() -> Model = {
  Model(items = marks, current = current)
}

val marksEventToIntent: Event.() -> Intent? = {
  when (this) {
    is Event.ItemClick -> TODO()
    is Event.DeleteClick -> TODO()
  }
}

val inputToMarksIntent: Input.() -> Intent? = {
  when (this) {
    is Input.AddNewMark -> Intent.HandleNewMark(circle, sliceNumber, cut)
    is Input.SelectMark -> Intent.SelectMark(mark)
    is Input.UnselectMark -> Intent.UnselectMark(mark)
  }
}

val marksLabelToMarksOutput: Label.() -> Output? = {
  when (this) {
    is Label.MarksLoaded -> Output.Marks(list)
  }
}
