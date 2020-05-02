package client.newmvi.researchlist.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.researchlist.store.ResearchListStore
import client.newmvi.researchlist.view.ResearchListView

class ResearchListBinder(
  private val store: ResearchListStore
) {

  private var disposables = CompositeDisposable()
  private var view: ResearchListView? = null

  fun attachView(view: ResearchListView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(ResearchListViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(ResearchListStateToViewModelMapper::invoke)
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