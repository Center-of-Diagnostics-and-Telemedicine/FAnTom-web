package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.*
import store.cut.MyCutStore.*

interface MyCutStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
    data class HandleBlackChanged(val blackValue: Int) : Intent()
    data class HandleWhiteChanged(val whiteValue: Int) : Intent()
    data class HandleGammaChanged(val gammaValue: Double) : Intent()
    data class HandleMipChanged(val mip: Mip) : Intent()
  }

  data class State(
    val sliceNumber: Int,
    val slice: String,
    val black: Int,
    val white: Int,
    val gamma: Double,
    val mipMethod: Mip,
    val mipValue: Int,
    val mainLoading: Boolean,
    val secondaryLoading: Boolean,
    val error: String
  ) : JvmSerializable

  sealed class Label {

    data class SliceNumberChanged(val sliceNumber: Int, val cut: Cut) : Label()
    data class ExternalSliceNumberChanged(val externalCut: Cut, val sliceNumber: Int) : Label()
    data class CircleDrawn(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Label()
    data class RectangleDrawn(
      val rectangle: Rectangle,
      val sliceNumber: Int,
      val cut: Cut
    ) : Label()

    data class Marks(val list: List<MarkModel>) : Label()
    data class ExpertMarks(val list: List<ExpertQuestionsModel>) : Label()
    data class SelectMark(val mark: MarkModel) : Label()
    data class UnselectMark(val mark: MarkModel) : Label()
    data class ContrastBrightnessChanged(val black: Int, val white: Int) : Label()
    data class UpdateMarkWithoutSave(val mark: MarkModel) : Label()
    data class UpdateMarkWithSave(val mark: MarkModel) : Label()
    data class StartClick(val startDicomX: Double, val startDicomY: Double) : Label()
    data class OpenFullCut(val cut: Cut) : Label()
    data class ChangeCutType(val cutType: CutType, val cut: Cut) : Label()

    object StopMoving : Label()
  }
}