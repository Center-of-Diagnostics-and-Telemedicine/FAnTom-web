package client.newmvi.menu.white.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.menu.white.store.WhiteStore
import client.newmvi.menu.white.view.WhiteView

class WhiteBinder(
  private val store: WhiteStore,
  private val whiteObservable: Observable<Double>
) {

  private var disposables = CompositeDisposable()
  private var view: WhiteView? = null

  fun attachView(view: WhiteView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(WhiteViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      whiteObservable
        .map {
          WhiteStore.Intent.NewValueFromSubscription(it)
        }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(WhiteStateToViewModelMapper::invoke)
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