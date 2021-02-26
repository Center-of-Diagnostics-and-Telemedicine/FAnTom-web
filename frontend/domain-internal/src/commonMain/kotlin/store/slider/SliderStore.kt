package store.slider

import com.arkivanov.mvikotlin.core.store.Store
import store.slider.SliderStore.Intent
import store.slider.SliderStore.State

interface SliderStore : Store<Intent, State, Nothing> {

  data class State(
    val currentValue: Int,
    val maxValue: Int,
    val defaultValue: Int
  )

  sealed class Intent {
    data class HandleChange(val value: Int) : Intent()
  }
}
