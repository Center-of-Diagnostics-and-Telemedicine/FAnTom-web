package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import model.Cut
import model.GET_SLICE_FAILED
import model.ResearchApiExceptions
import repository.ResearchRepository
import store.cut.CutStore.Intent
import store.cut.CutStore.State
import store.cut.CutStoreAbstractFactory

internal class CutStoreFactory(
  storeFactory: StoreFactory,
  val cut: Cut,
  val repository: ResearchRepository,
  val researchId: Int
) : CutStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  researchId = researchId
) {
  override fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {

    override fun executeAction(action: Unit, getState: () -> State) = load(getState)

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleSliceNumberChange -> dispatch(Result.SliceNumberChanged(intent.sliceNumber))
        is Intent.HandleBlackChanged -> dispatch(Result.BlackChanged(intent.blackValue))
        is Intent.HandleWhiteChanged -> dispatch(Result.WhiteChanged(intent.whiteValue))
        is Intent.HandleGammaChanged -> dispatch(Result.GammaChanged(intent.gammaValue))
        is Intent.HandleMipChanged -> dispatch(Result.MipChanged(intent.mip))
        is Intent.HandleMipValueChanged -> dispatch(Result.MipValueChanged(intent.mipValue))
        is Intent.HandlePresetChanged ->
          dispatch(Result.PresetChanged(black = intent.presets.black, white = intent.presets.white))
      }.let {}
    }

    private fun load(getState: () -> State) {
      singleFromCoroutine {
        val state = getState()
        repository.getSlice(
          researchId = researchId,
          black = state.black,
          white = state.white,
          gamma = state.gamma,
          type = cut.type.intType,
          mipMethod = state.mipMethod.intValue,
          sliceNumber = state.sliceNumber,
          aproxSize = state.mipValue
        )
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::dispatch,
          onError = ::handleError
        )
    }

    private fun handleError(error: Throwable) {
      val result = when (error) {
        is ResearchApiExceptions.SliceFetchException -> Result.Error(error.error)
        is ResearchApiExceptions.IncorrectSliceNumberException -> Result.Error(error.error)
        is ResearchApiExceptions.ResearchNotFoundException -> Result.Error(error.error)
        else -> {
          println("cut: other exception ${error.message}")
          Result.Error(GET_SLICE_FAILED)
        }
      }
      dispatch(result)
    }
  }

}
