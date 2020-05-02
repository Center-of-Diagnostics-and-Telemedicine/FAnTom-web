package client.newmvi.cut.store

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import client.newmvi.cut.store.CutStore.Intent
import client.newmvi.cut.store.CutStore.State

import model.SliceData
import model.initialSliceData

class CutStoreImpl(
  private val loader: CutLoader,
  cutType: Int
) : CutStore {

  private val _states = BehaviorSubject(State(url = "", sliceData = initialSliceData(cutType)))
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
      is Intent.DismissError -> {
        onResult(Effect.DismissErrorRequested)
        null
      }
      is Intent.GetUrl -> {
        onResult(Effect.UpdateSliceData(intent.sliceData))
        load(intent.sliceData)
      }
    }

  private fun load(sliceData: SliceData): Disposable? =
    if (state.isLoading) {
      null
    } else {
      onResult(Effect.LoadingStarted)

      loader
        .load(sliceData)
        .observeOn(computationScheduler)
        .map {
          when (it) {
            is CutLoader.Result.Success -> {
              Effect.Loaded(it.url)
            }
            is CutLoader.Result.Error -> {
              Effect.LoadingFailed(it.message)
            }
          }
        }
        .observeOn(mainScheduler)
        .subscribe(onSuccess = ::onResult)
    }

  private fun onResult(effect: Effect) {
    _states.onNext(Reducer(effect, _states.value))
  }

  private sealed class Effect {
    object LoadingStarted : Effect()
    class Loaded(val url: String) : Effect()
    data class LoadingFailed(val error: String) : Effect()

    class UpdateSliceData(val sliceData: SliceData) : CutStoreImpl.Effect()
    object DismissErrorRequested : Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.LoadingStarted -> state.copy(isLoading = true, error = "")
        is Effect.Loaded -> state.copy(isLoading = false, error = "", url = effect.url)
        is Effect.LoadingFailed -> state.copy(isLoading = false, error = effect.error)
        is Effect.DismissErrorRequested -> state.copy(error = "")
        is Effect.UpdateSliceData -> state.copy(sliceData = effect.sliceData)
      }
  }
}