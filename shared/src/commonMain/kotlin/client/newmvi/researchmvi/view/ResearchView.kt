package client.newmvi.researchmvi.view

import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView
import com.badoo.reaktive.subject.publish.PublishSubject
import model.*

interface ResearchView : BaseView<ResearchView.Event> {

  val events: PublishSubject<Event>

  fun show(model: ResearchViewModel)

  data class ResearchViewModel(
    val isLoading: Boolean,
    val error: String,
    val data: ResearchSlicesSizesData?,
    val gridModel: GridModel,
    val studyCompleted: Boolean,
    val ctTypeToConfirm: CTType?
  )

  sealed class Event : BaseEvent {
    data class Init(val researchId: Int) : Event()
    class GridChanged(val type: CutsGridType) : Event()
    class CellFullMode(val cellModel: CellModel) : Event()
    class CTTypeChosen(val ctType: CTType) : Event()
    class ConfirmCtType(
      val ctType: CTTypeModel,
      val leftPercent: String,
      val rightPercent: String
    ) : Event()

    object ErrorShown : Event()
    object Delete : Event()
    //    object Close : Event()
    object Clear : Event()

    object Back : Event()
  }
}