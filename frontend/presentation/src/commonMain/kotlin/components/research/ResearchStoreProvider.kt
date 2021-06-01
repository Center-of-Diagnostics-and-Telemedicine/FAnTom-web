package components.research

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import com.badoo.reaktive.utils.ensureNeverFrozen
import com.badoo.reaktive.utils.printStack
import model.BASE_ERROR
import model.ResearchApiExceptions
import model.ResearchSlicesSizesDataNew
import repository.ResearchRepository
import store.research.ResearchStore
import store.research.ResearchStore.*

internal class ResearchStoreProvider(
  private val storeFactory: StoreFactory,
  private val repository: ResearchRepository,
  private val researchId: Int
) {

  fun provide(): ResearchStore =
    object : ResearchStore, Store<Intent, State, Label> by storeFactory.create(
      name = "ResearchStore",
      initialState = State(),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    object Loading : Result()
    data class Loaded(val data: ResearchSlicesSizesDataNew) : Result()
    data class Error(val error: String) : Result()

    object DismissErrorRequested : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {
    override fun executeAction(action: Unit, getState: () -> State) = load()

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
        Intent.ReloadRequested -> load()
        Intent.BackRequested -> handleBackToList()
        Intent.CloseRequested -> handleCloseResearch()
      }.let {}
    }

    private fun handleBackToList() {
      singleFromCoroutine {
        repository.closeSession(researchId)
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = { publish(Label.Back) },
          onError = {
            handleError(it)
            publish(Label.Back)
          }
        )
    }

    private fun handleCloseResearch() {
      singleFromCoroutine {
        repository.closeResearch(researchId = researchId)
        repository.closeSession(researchId = researchId)
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = { publish(Label.Back) },
          onError = ::handleError
        )
    }

    private fun load() {
      dispatch(Result.Loading)
      singleFromCoroutine {
        repository.initResearch(
          researchId = researchId,
          doseReport = false//research.category == DOSE_REPORT_RESEARCH_CATEGORY
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
        is ResearchApiExceptions -> Result.Error(error.error)
        else -> {
          println(error.printStack())
          println("login: other exception ${error.message}")
          Result.Error(BASE_ERROR)
        }
      }
      dispatch(result)
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.Loaded -> copy(data = result.data, loading = false)
        is Result.Error -> copy(error = result.error, loading = false)
        is Result.DismissErrorRequested -> copy(error = "")
      }
  }
}