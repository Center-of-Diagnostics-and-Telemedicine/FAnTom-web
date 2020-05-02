package client.newmvi.menu.table.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import client.newmvi.menu.table.store.TableStore
import client.newmvi.menu.table.view.TableView
import model.SelectedArea

class TableBinder(
  private val store: TableStore,
  private val areaObservable: BehaviorSubject<List<SelectedArea>>,
  private val selectedAreaObservable: Observable<Int>
) {

  private var disposables = CompositeDisposable()
  private var view: TableView? = null

  fun attachView(view: TableView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(TableViewEventToIntentMapper::invoke)
        .subscribe( onNext = store::accept))

    disposables.add(
      areaObservable
        .map { TableStore.Intent.Areas(it) }
        .subscribe( onNext = store::accept))

    disposables.add(
      store
        .states
        .map(TableStateToViewModelMapper::invoke)
        .subscribe( onNext = { requireNotNull(view).show(it) }))

    disposables.add(
      selectedAreaObservable
        .map {
          TableStore.Intent.NewSelectedAreaIdIncome(it)
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