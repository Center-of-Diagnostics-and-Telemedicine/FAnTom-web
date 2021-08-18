package components.cut

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.plusAssign
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.doOnBeforeSubscribe
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.BASE_ERROR
import model.HasIntValue
import model.MyPlane
import model.ResearchApiExceptions
import repository.GetSliceModel
import repository.MyResearchRepository
import store.cut.CutModel
import store.cut.MyCutStore
import store.cut.MyCutStore.*

class MyCutStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchRepository: MyResearchRepository,
  private val researchId: Int,
  private val plane: MyPlane
) {

  fun provide(): MyCutStore =
    object : MyCutStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MyCutStore_${researchId}_${plane.type}",
      initialState = State(cutModel = null),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun executeAction(action: Unit, getState: () -> State) {
      scope { disposable.dispose() }
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleChangeCutModel -> handleChangeCutModel(intent.cutModel, getState())
      }.let {}
    }

    private fun handleChangeCutModel(cutModel: CutModel, state: State) {
      if (state.cutModel != cutModel) {
        disposable.clear(true)
        disposable += singleFromCoroutine {
          researchRepository.getSlice(cutModel.toGetSliceModel())
        }
          .doOnBeforeSubscribe {
            dispatch(Result.CutModelChanged(cutModel))
            dispatch(Result.Loading)
          }
          .subscribeOn(ioScheduler)
          .map(Result::Loaded)
          .observeOn(mainScheduler)
          .subscribeScoped(onSuccess = ::dispatch, onError = ::handleError)
      }
    }

    private fun handleError(error: Throwable) {
      val result = when (error) {
        is ResearchApiExceptions -> Result.Error(error.error)
        else -> {
          println("cut: other exception ${error.message}")
          Result.Error(BASE_ERROR)
        }
      }
      dispatch(result)
    }
  }

  private sealed class Result : JvmSerializable {
    object Loading : Result()
    data class CutModelChanged(val cutModel: CutModel) : Result()
    data class Loaded(val slice: String) : Result()
    data class Error(val message: String) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.CutModelChanged -> copy(cutModel = result.cutModel)
        is Result.Loaded -> copy(loading = false, error = "", slice = result.slice)
        is Result.Error -> copy(error = result.message, loading = false)
      }
  }

  private fun CutModel.toGetSliceModel(): GetSliceModel =
    GetSliceModel(
      researchId = researchId,
      black = blackValue,
      white = whiteValue,
      gamma = gammaValue,
      type = plane.type,
      mipMethod = mip.intValue,
      aproxSize = (mip as? HasIntValue)?.value ?: 0,
      width = if (width > height) 0 else width,
      height = if (height > width) 0 else height,
      sliceNumber = sliceNumber,
      sopInstanceUid = plane.data.SOPInstanceUID ?: "",
      modality = plane.researchType.name
    )
}