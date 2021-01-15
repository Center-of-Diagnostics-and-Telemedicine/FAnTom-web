package client.newmvi.menu.mipmethod.store

import client.debugLog
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import client.newmvi.menu.mipmethod.store.MipMethodStore.Intent
import client.newmvi.menu.mipmethod.store.MipMethodStore.State

class MipMethodStoreImpl(
  private val mipMethodObservable: Subject<Int>
) : MipMethodStore {

  private val _states = BehaviorSubject(State(value = 0))
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
      is Intent.ChangeMethod -> {
        if (state.value != intent.value) {
          debugLog("mip method changed ${intent.value}")
          mipMethodObservable.onNext(intent.value)
        }
        null
      }
      is Intent.NewValueFromSubscription -> {
        onResult(Effect.Value(intent.value))
        null
      }
    }

  private fun onResult(effect: Effect) {
    _states.onNext(Reducer(effect, _states.value))
  }

  private sealed class Effect {
    data class Value(val value: Int) : Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.Value -> state.copy(value = effect.value)
      }
  }
}