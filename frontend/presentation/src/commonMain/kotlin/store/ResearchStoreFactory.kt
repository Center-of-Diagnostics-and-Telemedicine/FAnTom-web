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
import model.RESEARCH_DATA_FETCH_FAILED
import model.ResearchApiExceptions
import repository.ResearchRepository
import store.ResearchStore.Intent
import store.ResearchStore.State

internal class ResearchStoreFactory(
  storeFactory: StoreFactory,
  private val repository: ResearchRepository,
  private val researchId: Int
) : ResearchStoreAbstractFactory(
  storeFactory = storeFactory
) {
  override fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
    override fun executeAction(action: Unit, getState: () -> State) = load()

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
        Intent.ReloadRequested -> load()
        Intent.CloseRequested -> TODO()
      }.let {}
    }

    private fun load() {
      singleFromCoroutine {
        repository.initResearch(researchId = researchId)
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
        is ResearchApiExceptions.ResearchInitializationException -> Result.Error(error.error)
        is ResearchApiExceptions.ResearchNotFoundException -> Result.Error(error.error)
        else -> {
          println("login: other exception ${error.message}")
          Result.Error(RESEARCH_DATA_FETCH_FAILED)
        }
      }
      dispatch(result)
    }
  }

}
