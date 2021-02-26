package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.Mip
import store.tools.MipStore.*

interface MipStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleMipClick(val mip: Mip) : Intent()
    data class HandleMipValueChanged(val value: Int) : Intent()
  }

  data class State(
    val list: List<Mip> = listOf(),
    val current: Mip = Mip.No,
    val currentValue: Int? = null
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    data class MipMethodChanged(val item: Mip) : Label()
    data class MipValueChanged(val value: Int) : Label()
  }
}
