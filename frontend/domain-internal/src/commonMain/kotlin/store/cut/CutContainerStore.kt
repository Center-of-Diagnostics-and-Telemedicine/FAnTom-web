package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Mip
import store.cut.CutContainerStore.*

interface CutContainerStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeSliceNumber(val sliceNumber: Int) : Intent()
    data class ChangeBlackValue(val value: Int) : Intent()
    data class ChangeWhiteValue(val value: Int) : Intent()
    data class ChangeGammaValue(val value: Double) : Intent()
    data class ChangeMipValue(val mip: Mip) : Intent()
  }

  data class State(
    val sliceNumber: Int,
    val blackValue: Int,
    val whiteValue: Int,
    val gammaValue: Double,
    val mip: Mip
  ) : JvmSerializable

  fun State.toCutModel(): CutModel = CutModel(
    sliceNumber = sliceNumber,
    blackValue = blackValue,
    whiteValue = whiteValue,
    gammaValue = gammaValue,
    mip = mip
  )

  sealed class Label {
    data class CutModelChanged(val cutModel: CutModel) : Label()
  }
}

data class CutModel(
  val sliceNumber: Int,
  val blackValue: Int,
  val whiteValue: Int,
  val gammaValue: Double,
  val mip: Mip
)

