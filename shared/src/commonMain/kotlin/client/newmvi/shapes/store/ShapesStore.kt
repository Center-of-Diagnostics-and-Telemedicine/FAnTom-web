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
    data class AreasIncome(val areas: List<CircleShape>) : Intent()
    data class UpdateLines(val lines: Lines) : Intent()
    data class UpdateMouseData(val positionData: PositionData) : Intent()
    data class UpdateSliceNumber(val sliceNumber: Int) : Intent()
    data class UpdateMoveRects(val moveRects: List<MoveRect>) : Intent()
    data class GetHounsfield(val positionData: PositionData): Intent()
    data class ChangeCutType(val type: CutType,val cellModel: CellModel) : Intent()

    object Idle : Intent()
  }
}