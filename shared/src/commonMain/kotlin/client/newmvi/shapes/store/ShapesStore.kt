package client.newmvi.shapes.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.*

interface ShapesStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val areas: List<CircleShape>,
    val horizontalLine: Line,
    val verticalLine: Line,
    val positionData: PositionData? = null,
    val cubeColor: String = "",
    val sliceNumber: Int = 1,
    val moveRects: List<MoveRect>,
    val huValue: Double? = null,
    val isLoading: Boolean = false
  )

  sealed class Intent {
    class AreasIncome(val areas: List<CircleShape>) : Intent()
    class UpdateLines(val lines: Lines) : Intent()
    class UpdateMouseData(val positionData: PositionData) : Intent()
    class UpdateSliceNumber(val sliceNumber: Int) : Intent()
    class UpdateMoveRects(val moveRects: List<MoveRect>) : Intent()
    class GetHounsfield(val positionData: PositionData): Intent()
    class ChangeCutType(val type: CutType,val cellModel: CellModel) : Intent()

    object Idle : Intent()
  }
}