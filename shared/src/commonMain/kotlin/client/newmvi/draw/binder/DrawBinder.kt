package client.newmvi.draw.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.draw.store.DrawStore
import client.newmvi.draw.view.DrawView

class DrawBinder(
  private val store: DrawStore
) {

  private var disposables = CompositeDisposable()
  private var view: DrawView? = null

  fun attachView(view: DrawView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(DrawViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(DrawStateToViewModelMapper::invoke)
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