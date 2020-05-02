package client.newmvi.slider.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.slider.store.SliderStore
import client.newmvi.slider.view.SliderView

class SliderBinder(
  private val store: SliderStore,
  private val sliceNumberObservable: Observable<Int>
) {

  private var disposables = CompositeDisposable()
  private var view: SliderView? = null

  fun attachView(view: SliderView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(SliderViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))


    disposables.add(
      sliceNumberObservable
        .map { SliderStore.Intent.NewValueFromSubscription(it) }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(SliderStateToViewModelMapper::invoke)
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