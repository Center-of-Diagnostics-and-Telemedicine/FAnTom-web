package client.newmvi.researchlist.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.Research

interface ResearchListStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: List<Research> = listOf()
  )

  sealed class Intent {
    object Init : Intent()
    object DismissError : Intent()
  }
}