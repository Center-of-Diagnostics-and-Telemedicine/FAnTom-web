package client.newmvi.menu.preset.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import model.Preset
import client.newmvi.menu.preset.store.PresetStore
import client.newmvi.menu.preset.view.PresetView

class PresetBinder(
  private val store: PresetStore,
  private val presetObservable: Observable<Preset>
) {

  private var disposables = CompositeDisposable()
  private var view: PresetView? = null

  fun attachView(view: PresetView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(PresetViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      presetObservable
        .map {
          PresetStore.Intent.NewValueFromSubscription(it)
        }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(PresetStateToViewModelMapper::invoke)
        .subscribe( onNext = { requireNotNull(view).show(it) }))
  }

  fun onStop() {
    disposables.clear()
  }

  fun detachView() {
    view = null
  }

  fun onDestroy() {
    store.dispose()
  }
}