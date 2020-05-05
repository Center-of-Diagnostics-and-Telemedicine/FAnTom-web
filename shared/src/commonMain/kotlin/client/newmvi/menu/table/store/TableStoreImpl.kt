package client.newmvi.menu.table.store

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import client.newmvi.menu.table.store.TableStore.Intent
import client.newmvi.menu.table.store.TableStore.State
import model.SelectedArea
import model.UpdateMarkModel

class TableStoreImpl(
  private val selectAreaListener: Subject<Int>,
  private val deleteAreaListener: Subject<Int>,
  private val updateMarkListener: Subject<UpdateMarkModel>
) : TableStore {

  private val _states = BehaviorSubject(State(areas = mutableListOf()))
  override val states: Observable<State> = _states
  private val state: State get() = _states.value

  private val disposables = CompositeDisposable()
  override val isDisposed: Boolean get() = disposables.isDisposed

  override fun dispose() {
    disposables.dispose()
    _states.onComplete()
  }

  override fun accept(intent: Intent) {
    execute(intent)?.also { disposables.add(it) }
  }

  private fun execute(intent: Intent): Disposable? =
    when (intent) {
      is Intent.Delete -> {
        deleteAreaListener.onNext(intent.selectedArea.id)
        null
      }
      is Intent.Areas -> {
        onResult(Effect.Value(intent.areas))
        null
      }
      is Intent.SelectArea -> {
        selectAreaListener.onNext(intent.areaId)
        null
      }
      is Intent.NewSelectedAreaIdIncome -> {
        onResult(Effect.Select(intent.areaId))
        null
      }
      is Intent.ChangeMarkType -> {
        updateMarkListener.onNext(UpdateMarkModel(id = intent.id, type = intent.type))
        null
      }
      is Intent.ChangeComment -> {
        updateMarkListener.onNext(UpdateMarkModel(id = intent.id, comment = intent.comment))
        null
      }
    }

  private fun onResult(effect: Effect) {
    _states.onNext(Reducer(effect, _states.value))
  }

  private sealed class Effect {
    data class Value(val selectedAreas: List<SelectedArea>) : Effect()
    class Select(val areaId: Int) : Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.Value -> state.copy(areas = effect.selectedAreas)
        is Effect.Select -> state.copy(selectedAreaId = effect.areaId)
      }
  }
}