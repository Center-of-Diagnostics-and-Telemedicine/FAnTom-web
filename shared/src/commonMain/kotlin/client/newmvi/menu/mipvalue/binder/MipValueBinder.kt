package client.newmvi.menu.mipvalue.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.menu.mipvalue.store.MipValueStore
import client.newmvi.menu.mipvalue.view.MipValueView

class MipValueBinder(
  private val store: MipValueStore,
  private val mipValueObservable: Observable<Int>,
  private val showMipValue: Observable<Boolean>
) {

  private var disposables = CompositeDisposable()
  private var view: MipValueView? = null

  fun attachView(view: MipValueView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(MipValueViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      mipValueObservable
        .map {
          MipValueStore.Intent.NewValueFromSubscription(
            it
          )
        }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(MipValueStateToViewModelMapper::invoke)
        .subscribe( onNext = { requireNotNull(view).show(it) }))

    disposables.add(
      showMipValue
        .map { show ->
          if (show)
            MipValueStore.Intent.EnableMip
          else
            MipValueStore.Intent.DisableMip
        }
        .subscribe( onNext = store::accept))
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