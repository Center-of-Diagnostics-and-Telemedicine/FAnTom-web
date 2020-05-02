package client.newmvi.shapes.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView
import model.*

interface ShapesView : BaseView<ShapesView.Event> {

  val events: PublishSubject<Event>

  fun show(model: ShapesViewModel)

  data class ShapesViewModel(
    val areas: List<CircleShape>,
    val horizontal: Line,
    val vertical: Line,
    val positionData: PositionData? = null,
    val cubeColor: String,
    val sliceNumber: Int,
    val moveRects: List<MoveRect>,
    val huValue: Double? = null
  )

  sealed class Event : BaseEvent {
    class ChangeCutType(val type: CutType, val cellModel: CellModel) : Event()

    object Idle : Event()
  }

}