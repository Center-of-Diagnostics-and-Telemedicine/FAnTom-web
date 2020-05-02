package client.newmvi.cut.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.SliceData

interface CutStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val url: String = "",
    val sliceData: SliceData
  )

  sealed class Intent {
    class GetUrl(val sliceData: SliceData) : Intent()

    object DismissError : Intent()
  }
}