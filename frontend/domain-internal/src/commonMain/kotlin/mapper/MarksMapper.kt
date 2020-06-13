package mapper

import controller.MarksController.Output
import store.tools.MarksStore.Intent
import store.tools.MarksStore.State
import view.MarksView.Event
import view.MarksView.Model

val marksStateToModel: State.() -> Model

val marksEventToIntent: Event.() -> Intent?
