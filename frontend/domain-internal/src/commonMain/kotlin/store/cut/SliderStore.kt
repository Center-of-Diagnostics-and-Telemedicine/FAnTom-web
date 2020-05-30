package store.cut

import com.arkivanov.mvikotlin.core.store.Store
import store.cut.SliderStore.*

interface SliderStore : Store<Intent, State, Label> {

  data class State(
    val currentValue: Int,
    val maxValue: Int,
    val defaultValue: Int
  )

  sealed class Intent {
    data class HandleChange(val value: Int) : Intent()
  }

  sealed class Label{
    data class SliceNumberChanged(val sliceNumber: Int): Label()
  }
}
