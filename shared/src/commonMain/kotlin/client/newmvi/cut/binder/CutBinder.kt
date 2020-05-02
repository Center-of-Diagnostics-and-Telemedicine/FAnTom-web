package client.newmvi.cut.binder

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.combineLatest
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe
import client.newmvi.ResearchContainer
import client.newmvi.cut.store.CutStore
import client.newmvi.cut.view.CutView
import model.SliceData
import model.SliceSizeData

class CutBinder(
  private val store: CutStore,
  private val sliceNumberObservable: Observable<Int>,
  private val cutType: Int,
  private val sliceSizeDataObservable: Observable<SliceSizeData>,
  private val blackAndWhiteObservable: Observable<Pair<Double, Double>>
) {

  private var disposables = CompositeDisposable()
  private var view: CutView? = null

  fun attachView(view: CutView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(CutViewEventToIntentMapper::invoke)
        .subscribe(onNext = store::accept)
    )

    disposables.add(
      combineLatest(
        ResearchContainer.researchIdObservable,
        blackAndWhiteObservable,
        ResearchContainer.gammaValueObservable,
        ResearchContainer.mipMethodObservable,
        ResearchContainer.mipValueObservable,
        sliceNumberObservable,
        sliceSizeDataObservable
      ) { researchId: Int,
          pairBlackAndWhite: Pair<Double, Double>,
          gamma: Double,
          mipMethod: Int,
          mipValue: Int,
          sliceNumber: Int,
          sliceSizeData: SliceSizeData ->
        SliceData(
          black = pairBlackAndWhite.first,
          white = pairBlackAndWhite.second,
          gamma = gamma,
          mipMethod = mipMethod,
          mipValue = mipValue,
          researchId = researchId,
          sliceNumber = sliceNumber,
          cutType = cutType,
          sliceSizeData = sliceSizeData
        )
      }
        .map {
          CutStore.Intent.GetUrl(it)
        }
        .subscribe(onNext = store::accept))

    disposables.add(
      store
        .states
        .map(CutStateToViewModelMapper::invoke)
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