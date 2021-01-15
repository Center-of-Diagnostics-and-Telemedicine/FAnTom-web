package client.newmvi.menu.mipmethod.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.menu.mipmethod.store.MipMethodStore
import client.newmvi.menu.mipmethod.view.MipMethodView

class MipMethodBinder(
  private val store: MipMethodStore,
  private val mipMethodObservable: Observable<Int>
) {

  private var disposables = CompositeDisposable()
  private var view: MipMethodView? = null

  fun attachView(view: MipMethodView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(MipMethodViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      mipMethodObservable
        .map {
          MipMethodStore.Intent.NewValueFromSubscription(it)
        }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(MipMethodStateToViewModelMapper::invoke)
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