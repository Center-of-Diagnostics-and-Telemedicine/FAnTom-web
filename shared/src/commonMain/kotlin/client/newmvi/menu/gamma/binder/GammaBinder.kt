package client.newmvi.menu.gamma.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.menu.gamma.store.GammaStore
import client.newmvi.menu.gamma.view.GammaView

class GammaBinder(
  private val store: GammaStore,
  private val GammaObservable: Observable<Double>
) {

  private var disposables = CompositeDisposable()
  private var view: GammaView? = null

  fun attachView(view: GammaView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(GammaViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      GammaObservable
        .map {
          GammaStore.Intent.NewValueFromSubscription(it)
        }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(GammaStateToViewModelMapper::invoke)
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