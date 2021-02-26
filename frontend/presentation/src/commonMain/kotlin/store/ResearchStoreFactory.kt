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
import model.BASE_ERROR
import model.ResearchApiExceptions
import repository.ResearchRepository
import store.research.ResearchStore.*
import store.research.ResearchStoreAbstractFactory

internal class ResearchStoreFactory(
  storeFactory: StoreFactory,
  private val repository: ResearchRepository,
  private val researchId: Int
) : ResearchStoreAbstractFactory(
  storeFactory = storeFactory
) {
  override fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

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
        repository.closeSession(researchId)
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
        is ResearchApiExceptions -> Result.Error(error.error)
        else -> {
          println("login: other exception ${error.message}")
          Result.Error(BASE_ERROR)
        }
      }
      dispatch(result)
    }
  }

}
