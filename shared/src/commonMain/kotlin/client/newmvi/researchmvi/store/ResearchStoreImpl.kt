package client.newmvi.researchmvi.store

import client.debugLog
import client.newmvi.researchmvi.store.ResearchStore.Intent
import client.newmvi.researchmvi.store.ResearchStore.State
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.*
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.*

class ResearchStoreImpl(
  private val researchDataLoader: ResearchDataLoader,
  private val slicesSizesDataListener: Subject<ResearchSlicesSizesData>,
  private val areaDeletedListener: Subject<Int>,
  private val areaSavedListener: Subject<Mark>,
  private val deleteClickObservable: Subject<Boolean>,
  private val gridProcessor: GridProcessor,
  private val callToCloseResearchListener: Subject<Boolean>,
  private val callBackToResearchListListener: Subject<Boolean>,
  private val confirmMarkProcessor: ConfirmCtTypeForResearchResearchProcessor,
  private val closeSessionProcessor: CloseSessionProcessor
) : ResearchStore {

  private val _states = BehaviorSubject(State(data = null, gridModel = initialGridModel()))
  override val states: Observable<State> = _states
  private val state: State get() = _states.value

  private val disposables = CompositeDisposable()
  override val isDisposed: Boolean get() = disposables.isDisposed

  override fun dispose() {
    disposables.dispose()
    _states.onComplete()
  }

  override fun accept(intent: Intent) {
    execute(intent)?.also { disposables.add(it) }
  }

  private fun execute(intent: Intent): Disposable? =
    when (intent) {
      is Intent.DismissError -> {
        onResult(Effect.DismissErrorRequested)
        null
      }
      is Intent.Init -> {
        loadData(intent.researchId)
      }
      is Intent.DeleteCalled -> {
        deleteClickObservable.onNext(true)
        null
      }
      is Intent.ChangeGrid -> {

        onResult(
          Effect.GridChanged(
            gridProcessor.changeGridType(
              newType = intent.type,
              currentModel = state.gridModel,
              sliceSizesData = state.data
            )
          )
        )
        null
      }
      is Intent.ChangeCutType -> {
        onResult(
          Effect.GridChanged(
            gridProcessor.changeCutType(
              newCutType = intent.newCutType,
              currentModel = state.gridModel,
              sliceSizesData = state.data
            )
          )
        )
        null
      }
      is Intent.OpenCell -> {
        val oldGridModel = state.oldGridModel
        if (state.gridModel.cells.size > 1 || state.oldGridModel != null) {
          if (oldGridModel == null) {
            onResult(Effect.OpenCutFullMode(gridProcessor.openCutFullMode(intent.cellModel)))
          } else {
            onResult(Effect.CloseCutFullMode(gridProcessor.closeCutFullMode(oldModel = oldGridModel)))
          }
        }
        null
      }
      Intent.CallToCloseResearch -> {
        callToCloseResearchListener.onNext(true)
        null
      }
      Intent.CallBackToResearchList -> {
        callBackToResearchListListener.onNext(true)
        null
      }
      Intent.Close -> {
        debugLog("Intent.Close income")
//        close(
//          intent.ctType,
//          intent.leftPercent,
//          intent.rightPercent
//       )
        null
      }//todo(использовать как метод для сохранения отметки)
      Intent.ShowAreasNotFull -> {
        onResult(Effect.AreaSaveError("Необходимо указать типы для всех очаговых образований"))
        null
      }
      Intent.BackToResearchList -> {
        closeSession()
      }
      Intent.Clear -> {
        onResult(Effect.ClearOldData)
        null
      }
      is Intent.CTTypeChosen -> {
        onResult(Effect.ConfirmCTType(intent.ctType))
        null
      }
      Intent.CloseSession -> {
        closeSession()
      }
      is Intent.ConfirmCTType -> {
        debugLog("Intent.ConfirmCTType income, left = ${intent.leftPercent}, right = ${intent.rightPercent}, cttype = ${intent.ctType.ordinal}")
        confirmCtType(intent.ctType, intent.leftPercent, intent.rightPercent)
      }
    }

  private fun confirmCtType(ctType: CTType, leftPercent: Int, rightPercent: Int): Disposable? {
    onResult(Effect.LoadingStarted)
    return confirmMarkProcessor
      .confirm(ctType, leftPercent, rightPercent, state.researchId)
      .observeOn(computationScheduler)
      .map {
        when (it) {
          is ConfirmCtTypeForResearchResearchProcessor.Result.Success -> Effect.Close
          is ConfirmCtTypeForResearchResearchProcessor.Result.Error -> Effect.ConfirmCTTypeFailed(it.message)
          is ConfirmCtTypeForResearchResearchProcessor.Result.SessionExpired ->
            Effect.LoadingFailed(SESSION_EXPIRED)
        }
      }
      .observeOn(mainScheduler)
      .subscribe(onSuccess = ::onResult)
  }


  private fun loadData(researchId: Int): Disposable? =
    if (state.isLoading) {
      null
    } else {
      onResult(Effect.LoadingStarted)

      researchDataLoader
        .load(researchId)
        .observeOn(computationScheduler)
        .map {
          when (it) {
            is ResearchDataLoader.Result.Success -> {
              slicesSizesDataListener.onNext(it.researchData)
              Effect.DataLoaded(it.researchData, gridModel = gridProcessor.init(it.researchData))
            }
            is ResearchDataLoader.Result.SessionExpired -> Effect.LoadingFailed(SESSION_EXPIRED)
            is ResearchDataLoader.Result.Error -> Effect.LoadingFailed(it.message)
          }
        }
        .observeOn(mainScheduler)
        .subscribe(onSuccess = ::onResult)
    }

  private fun closeSession(): Disposable? {
    onResult(Effect.LoadingStarted)
    return closeSessionProcessor
      .close(state.researchId)
      .observeOn(computationScheduler)
      .map { Effect.Close }
      .observeOn(mainScheduler)
      .subscribe(onSuccess = ::onResult)
  }

  private fun onResult(effect: Effect) {
    _states.onNext(Reducer(effect, _states.value))
  }

  private sealed class Effect {
    object LoadingStarted : Effect()
    class DataLoaded(val data: ResearchSlicesSizesData?, val gridModel: GridModel) : Effect()
    object MarksLoaded : Effect()
    data class LoadingFailed(val error: String) : Effect()
    object DismissErrorRequested : Effect()

    object AreaSaved : Effect()
    class AreaSaveError(val message: String) : Effect()

    object AreaDeleted : Effect()
    class AreaDeleteError(val message: String) : Effect()

    object AreaUpdated : Effect()
    class AreaUpdateError(val message: String) : Effect()
    class GridChanged(val newGridModel: GridModel) : Effect()

    object Close : Effect()
    object ClearOldData : Effect()

    class AreasNotFullError(val message: String) : Effect()
    class OpenCutFullMode(val gridModel: GridModel) : Effect()
    class CloseCutFullMode(val gridModel: GridModel) : Effect()

    class ConfirmCTType(val ctType: CTType) : Effect()
    class ConfirmCTTypeFailed(val error: String) : Effect()
    class SessionExpired(val error: String) : Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.LoadingStarted -> state.copy(isLoading = true, error = "")
        is Effect.DataLoaded -> state.copy(
          isLoading = false,
          error = "",
          data = effect.data,
          researchId = effect.data?.researchId ?: -1,
          gridModel = effect.gridModel
        )
        is Effect.LoadingFailed -> state.copy(isLoading = false, error = effect.error)
        is Effect.DismissErrorRequested -> state.copy(error = "")
        is Effect.MarksLoaded -> state
        is Effect.AreaDeleted -> state.copy(isLoading = false)
        is Effect.AreaDeleteError -> state.copy(isLoading = false, error = effect.message)
        is Effect.AreaSaved -> state.copy(isLoading = false)
        is Effect.AreaSaveError -> state.copy(isLoading = false, error = effect.message)
        is Effect.AreaUpdated -> state.copy(isLoading = false)
        is Effect.AreaUpdateError -> state.copy(isLoading = false, error = effect.message)
        is Effect.GridChanged -> state.copy(gridModel = effect.newGridModel)
        is Effect.OpenCutFullMode -> state.copy(
          gridModel = effect.gridModel,
          oldGridModel = state.gridModel
        )
        is Effect.CloseCutFullMode -> state.copy(gridModel = effect.gridModel, oldGridModel = null)
        is Effect.Close -> state.copy(studyCompleted = true)
        is Effect.AreasNotFullError -> state.copy(studyCompleted = false, error = effect.message)
        Effect.ClearOldData -> State(data = null, gridModel = initialGridModel())
        is Effect.ConfirmCTType -> state.copy(ctTypeToConfirm = effect.ctType)
        is Effect.ConfirmCTTypeFailed -> state.copy(
          ctTypeToConfirm = null,
          isLoading = false,
          error = effect.error
        )
        is Effect.SessionExpired -> state.copy(
          ctTypeToConfirm = null,
          error = effect.error,
          isLoading = false
        )
      }
  }
}
