package components.cut

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import repository.GetSliceModel
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.MyResearchRepository
import store.cut.MyCutStore
import store.cut.MyCutStore.*

class MyCutStoreProvider(
  private val storeFactory: StoreFactory,
  private val brightnessRepository: MyBrightnessRepository,
  private val researchRepository: MyResearchRepository,
  private val mipRepository: MyMipRepository,
  private val researchId: Int,
  private val plane: Plane
) {

  fun provide(): MyCutStore =
    object : MyCutStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CutStore_${researchId}_${plane.type.intType}",
      initialState = State(),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      println("executeAction")
      combineLatest(
        brightnessRepository.black,
        brightnessRepository.white,
        brightnessRepository.gamma,
        mipRepository.mipMethod,
        mapper,
      )
        .switchMapSingle { model ->
          singleFromCoroutine {
            researchRepository.getSlice(model)
          }
        }
        .subscribeOn(ioScheduler)
        .doOnBeforeNext { dispatch(Result.Loading) }
        .doOnBeforeSubscribe { dispatch(Result.Loading) }
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        else -> throw NotImplementedError("Intent not implemented in MyCutStoreProvider $intent")
      }.let {}
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
    data class Loaded(val slice: String) : Result()
    data class Error(val message: String) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.Loaded -> copy(loading = false, error = "", slice = result.slice)
        is Result.Error -> copy(error = result.message, loading = false)
      }
  }

  private val mapper: (black: Int, white: Int, gamma: Double, mip: Mip) -> GetSliceModel =
    { black, white, gamma, mip ->
      GetSliceModel(
        researchId = researchId,
        black = black,
        white = white,
        gamma = gamma,
        type = plane.type.intType,
        mipMethod = mip.intValue,
        aproxSize = (mip as? HasIntValue)?.value ?: 0,
        width = 0,
        height = 0,
        sliceNumber = 1,
        sopInstanceUid = plane.data.SOPInstanceUID ?: ""
      )
    }
}