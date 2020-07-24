package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.*
import store.cut.CutStore.*

interface CutStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {

    data class HandleSliceNumberChange(val sliceNumber: Int) : Intent()
    data class HandleBlackChanged(val blackValue: Int) : Intent()
    data class HandleWhiteChanged(val whiteValue: Int) : Intent()
    data class HandleGammaChanged(val gammaValue: Double) : Intent()
    data class HandleMipChanged(val mip: Mip) : Intent()
    data class HandleMipValueChanged(val mipValue: Int) : Intent()
    data class HandlePresetChanged(val presets: Presets) : Intent()
    data class HandleCircleDrawn(val circle: Circle) : Intent()
    data class HandleExternalSliceNumberChanged(val externalCut: Cut, val sliceNumber: Int) :
      Intent()

    data class HandleMarks(val list: List<MarkModel>) : Intent()
    data class HandleMarkSelected(val mark: MarkModel) : Intent()
    data class HandleMarkCenter(val mark: MarkModel) : Intent()
    data class ChangeSliceNumberByMarkCenter(val mark: MarkModel) : Intent()
    data class HandleMarkUnselect(val mark: MarkModel) : Intent()

    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Intent()
    data class ChangeSliceNumberByDraw(val deltaDicomY: Int) : Intent()

    data class HandleMarkUpdateWithoutSave(val mark: MarkModel) : Intent()
    data class HandleMarkUpdateWithSave(val mark: MarkModel) : Intent()
    data class HandleStartClick(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class HandleChangeCutType(val cutType: CutType) : Intent()

    object ContrasBrightnessChanged : Intent()
    object HandleStopMoving : Intent()
    object OpenFullCut : Intent()
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
    data class Marks(val list: List<MarkModel>) : Label()
    data class SelectMark(val mark: MarkModel) : Label()
    data class CenterMark(val mark: MarkModel) : Label()
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
