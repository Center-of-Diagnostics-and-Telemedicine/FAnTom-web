package client.newmvi.menu.mipvalue.store

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.INITIAL_MIP_VALUE
import client.newmvi.menu.mipvalue.store.MipValueStore.Intent
import client.newmvi.menu.mipvalue.store.MipValueStore.State

class MipValueStoreImpl(
  private val mipValueObservable: Subject<Int>
) : MipValueStore {

  private val _states = BehaviorSubject(State(value = INITIAL_MIP_VALUE))
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
      is Intent.ChangeValue -> {
        mipValueObservable.onNext(intent.value)
        null
      }
      is Intent.NewValueFromSubscription -> {
        onResult(Effect.Value(intent.value))
        null
      }
      Intent.EnableMip -> {
        onResult(Effect.Enable)
        null
      }
      Intent.DisableMip -> {
        onResult(Effect.Disable)
        null
      }
    }

  private fun onResult(effect: Effect) {
    _states.onNext(Reducer(effect, _states.value))
  }

  private sealed class Effect {
    data class Value(val value: Int) : Effect()
    object Enable: Effect()
    object Disable: Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.Value -> state.copy(value = effect.value)
        is Effect.Enable -> state.copy(available = true)
        is Effect.Disable -> state.copy(available = false)
      }
  }
}