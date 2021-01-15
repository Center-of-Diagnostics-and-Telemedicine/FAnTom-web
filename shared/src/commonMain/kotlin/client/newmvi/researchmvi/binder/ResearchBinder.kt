package client.newmvi.researchmvi.binder

import client.newmvi.researchmvi.store.ResearchStore
import client.newmvi.researchmvi.view.ResearchView
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.*
import model.*

class ResearchBinder(
  private val store: ResearchStore,
  private val changeCutTypeModelObservable: Observable<ChangeCutTypeModel>,
  private val closeResearchObservable: Observable<CloseCommands>
) {

  private var disposables = CompositeDisposable()
  private var view: ResearchView? = null

  fun attachView(view: ResearchView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(ResearchViewEventToIntentMapper::invoke)
        .subscribe(onNext = store::accept)
    )

    disposables.add(
      store
        .states
        .map(ResearchStateToViewModelMapper::invoke)
        .subscribe(onNext = { requireNotNull(view).show(it) })
    )

    disposables.add(
      changeCutTypeModelObservable
        .map { ResearchStore.Intent.ChangeCutType(it) }
        .subscribe(onNext = store::accept))

    disposables.add(
      closeResearchObservable
        .map(CloseCommandToIntentMapper::invoke)
        .subscribe(onNext = store::accept)
    )
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