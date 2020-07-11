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

    data class HandleMarks(val list: List<MarkDomain>) : Intent()
    data class HandleMarkSelected(val mark: MarkDomain) : Intent()
    data class HandleMarkCenter(val mark: MarkDomain) : Intent()
    data class ChangeSliceNumberByMarkCenter(val mark: MarkDomain) : Intent()
    data class HandleMarkUnselect(val mark: MarkDomain) : Intent()

    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Intent()
    data class ChangeSliceNumberByDraw(val deltaDicomY: Int) : Intent()

    data class HandleMarkUpdate(val mark: MarkDomain) : Intent()

    object ContrasBrightnessChanged : Intent()
  }

  data class State(
    val sliceNumber: Int,
    val slice: String,
    val black: Int,
    val white: Int,
    val gamma: Double,
    val mipMethod: Mip,
    val mipValue: Int,
    val loading: Boolean,
    val error: String
  ) : JvmSerializable

  sealed class Label {
    data class SliceNumberChanged(val sliceNumber: Int, val cut: Cut) : Label()
    data class ExternalSliceNumberChanged(val externalCut: Cut, val sliceNumber: Int) : Label()
    data class CircleDrawn(val circle: Circle, val sliceNumber: Int, val cut: Cut) : Label()
    data class Marks(val list: List<MarkDomain>) : Label()
    data class SelectMark(val mark: MarkDomain) : Label()
    data class CenterMark(val mark: MarkDomain) : Label()
    data class UnselectMark(val mark: MarkDomain) : Label()
    data class ContrastBrightnessChanged(val black: Int, val white: Int) : Label()
    data class MarkUpdate(val mark: MarkDomain) : Label()
  }
}
