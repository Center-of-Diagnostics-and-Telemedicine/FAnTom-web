package client.newmvi.researchmvi.store

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribe
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.CTType
import client.newmvi.cut.store.AreaDeleter
import client.newmvi.cut.store.AreaSaver
import client.newmvi.cut.store.AreaUpdater
import client.newmvi.researchmvi.store.ResearchStore.Intent
import client.newmvi.researchmvi.store.ResearchStore.State
import model.*

class ResearchStoreImpl(
  private val researchDataLoader: ResearchDataLoader,
  private val researchMarksLoader: ResearchMarksLoader,
  private val slicesSizesDataListener: Subject<ResearchSlicesSizesData>,
  private val marksListener: Subject<List<SelectedArea>>,
  private val areaDeleter: AreaDeleter,
  private val areaDeletedListener: Subject<Int>,
  private val areaSaver: AreaSaver,
  private val areaSavedListener: Subject<SelectedArea>,
  private val areaUpdater: AreaUpdater,
  private val deleteClickObservable: Subject<Boolean>,
  private val gridProcessor: GridProcessor,
  private val callToCloseResearchListener: Subject<Boolean>,
  private val callBackToResearchListListener: Subject<Boolean>,
  private val closeResearchProcessor: CloseResearchProcessor
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
      is Intent.DeleteMark -> delete(intent.areaId)
      is Intent.SaveMark -> save(intent.areaToSave)
      is Intent.UpdateMark -> update(intent.areaToUpdate)
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
      Intent.Close -> close()
      Intent.ShowAreasNotFull -> {
        onResult(Effect.AreaSaveError("Необходимо указать типы для всех очаговых образований"))
        null
      }
      Intent.BackToResearchList -> {
        onResult(Effect.Close)
        null
      }
      Intent.Clear -> {
        onResult(Effect.ClearOldData)
        null
      }
      is Intent.CTTypeChosen -> {
        onResult(Effect.ConfirmCTType(intent.ctType))
        null
      }
    }

  private fun close(): Disposable? =
    if (state.isLoading) {
      null
    } else {
      onResult(Effect.LoadingStarted)
      closeResearchProcessor
        .close(state.researchId)
        .observeOn(computationScheduler)
        .map {
          when (it) {
            is CloseResearchProcessor.Result.Success -> {
              Effect.Close
            }
            is CloseResearchProcessor.Result.Error -> Effect.LoadingFailed(it.message)
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
//              loadMarks(researchId)
              Effect.DataLoaded(it.researchData, gridModel = gridProcessor.init(it.researchData))
            }
            is ResearchDataLoader.Result.Error -> Effect.LoadingFailed(it.message)
          }
        }
        .observeOn(mainScheduler)
        .subscribe(onSuccess = ::onResult)
    }

  private fun loadMarks(researchId: Int) {
    researchMarksLoader
      .load(researchId)
      .observeOn(computationScheduler)
      .map {
        when (it) {
          is ResearchMarksLoader.Result.Success -> {
            marksListener.onNext(it.marks)
            Effect.MarksLoaded
          }
          is ResearchMarksLoader.Result.Error -> Effect.LoadingFailed(it.message)
        }
      }
      .observeOn(mainScheduler)
      .subscribe(onSuccess = ::onResult)
  }

  private fun save(areaToSave: AreaToSave): Disposable? =
    areaSaver
      .save(areaToSave, state.researchId)
      .observeOn(computationScheduler)
      .map {
        when (it) {
          is AreaSaver.Result.Success -> {
            areaSavedListener.onNext(it.areaWithId)
            Effect.AreaSaved
          }
          is AreaSaver.Result.Error -> Effect.AreaSaveError(it.message)
        }
      }
      .observeOn(mainScheduler)
      .subscribe(onSuccess = ::onResult)

  private fun update(areaToUpdate: SelectedArea): Disposable? =
    areaUpdater
      .update(areaToUpdate)
      .observeOn(computationScheduler)
      .map {
        when (it) {
          is AreaUpdater.Result.Success -> {
            areaSavedListener.onNext(it.area)
            Effect.AreaUpdated
          }
          is AreaUpdater.Result.Error -> Effect.AreaDeleteError(it.message)
        }
      }
      .observeOn(mainScheduler)
      .subscribe(onSuccess = ::onResult)

  private fun delete(areaId: Int): Disposable? =
    areaDeleter
      .delete(areaId)
      .observeOn(computationScheduler)
      .map {
        when (it) {
          is AreaDeleter.Result.Success -> {
            areaDeletedListener.onNext(it.deletedAreaId)
            Effect.AreaDeleted
          }
          is AreaDeleter.Result.Error -> Effect.AreaDeleteError(it.message)
        }
      }
      .observeOn(mainScheduler)
      .subscribe(onSuccess = ::onResult)

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

    class ConfirmCTType(val ctType: CTType): Effect()
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
      }
  }
}