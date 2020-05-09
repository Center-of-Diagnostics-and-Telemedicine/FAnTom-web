package client.newmvi.shapes.binder

import client.newmvi.shapes.store.ShapesStore
import client.newmvi.shapes.view.ShapesView
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.mainScheduler
import model.*

class ShapesBinder(
  private val store: ShapesStore,
  private val areasObservable: Observable<List<CircleShape>>,
  private val linesObservable: Observable<Lines>,
  private val mouseDataObservable: Observable<PositionData>,
  private val sliceNumberObservable: Observable<Int>,
  private val moveRectsObservable: Observable<List<MoveRect>>
) {

  private var disposables = CompositeDisposable()
  private var view: ShapesView? = null

  fun attachView(view: ShapesView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(ShapesViewEventToIntentMapper::invoke)
        .subscribe(onNext = store::accept)
    )

    disposables.add(
      linesObservable
        .map { ShapesStore.Intent.UpdateLines(it) }
        .subscribe(onNext = store::accept))

    disposables.add(
      mouseDataObservable
        .map { ShapesStore.Intent.UpdateMouseData(it) }
        .subscribe(onNext = store::accept))

    disposables.add(
      mouseDataObservable
        .debounce(60, mainScheduler)
        .map {
          ShapesStore.Intent.GetHounsfield(it)
        }
        .subscribe(onNext = store::accept))

    disposables.add(
      sliceNumberObservable
        .map { ShapesStore.Intent.UpdateSliceNumber(it) }
        .subscribe(onNext = store::accept))

//    disposables.add(
//      moveRectsObservable
//        .map { ShapesStore.Intent.UpdateMoveRects(it) }
//        .subscribe(onNext = store::accept))

    disposables.add(
      store
        .states
        .map(ShapesStateToViewModelMapper::invoke)
        .subscribe(onNext = { requireNotNull(view).show(it) })
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