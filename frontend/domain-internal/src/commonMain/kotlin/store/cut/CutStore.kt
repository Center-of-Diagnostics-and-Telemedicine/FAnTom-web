package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Circle
import model.Cut
import model.Mip
import model.Presets
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
  }
}
