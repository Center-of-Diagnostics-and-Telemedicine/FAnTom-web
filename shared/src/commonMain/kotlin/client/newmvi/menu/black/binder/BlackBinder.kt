package client.newmvi.menu.black.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.menu.black.store.BlackStore
import client.newmvi.menu.black.view.BlackView

class BlackBinder(
  private val store: BlackStore,
  private val BlackObservable: Observable<Double>
) {

  private var disposables = CompositeDisposable()
  private var view: BlackView? = null

  fun attachView(view: BlackView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(BlackViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      BlackObservable
        .map {
          BlackStore.Intent.NewValueFromSubscription(it)
        }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(BlackStateToViewModelMapper::invoke)
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