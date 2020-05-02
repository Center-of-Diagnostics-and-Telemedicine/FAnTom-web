package client.newmvi.menu.preset.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.Preset

interface PresetStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val value: Preset = Preset.PRESET_SOFT_TISSUE
  )

  sealed class Intent {
    data class ChangePreset(val value: Preset): Intent()
    data class NewValueFromSubscription(val value: Preset): Intent()
  }
}