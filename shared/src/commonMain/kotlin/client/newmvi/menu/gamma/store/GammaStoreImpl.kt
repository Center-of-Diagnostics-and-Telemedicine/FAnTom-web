package client.newmvi.menu.gamma.store

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.INITIAL_GAMMA
import client.newmvi.menu.gamma.store.GammaStore.Intent
import client.newmvi.menu.gamma.store.GammaStore.State

class GammaStoreImpl(
  private val gammaObservable: Subject<Double>
) : GammaStore {

  private val _states = BehaviorSubject(State(value = INITIAL_GAMMA))
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
      is Intent.Drag -> {
        gammaObservable.onNext(intent.value)
        null
      }
      is Intent.NewValueFromSubscription -> {
        onResult(Effect.Value(intent.value))
        null
      }
    }

  private fun onResult(effect: Effect) {
    _states.onNext(
      Reducer(
        effect,
        _states.value
      )
    )
  }

  private sealed class Effect {
    data class Value(val value: Double) : Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.Value -> state.copy(value = effect.value)
      }
  }
}