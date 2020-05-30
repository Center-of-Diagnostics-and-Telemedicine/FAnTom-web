package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import store.tools.BrightnessStore.*

interface BrightnessStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleBlackChanged(val value: Int) : Intent()
    data class HandleWhiteValueChanged(val value: Int) : Intent()
    data class HandleGammaValueChanged(val value: Double) : Intent()
  }

  data class State(
    val blackValue: Int,
    val whiteValue: Int,
    val gammaValue: Double
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class BlackChanged(val value: Int) : Label()
    data class WhiteChanged(val value: Int) : Label()
    data class GammaChanged(val value: Double) : Label()
  }
}
