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
import model.MARKS_FETCH_EXCEPTION
import model.ResearchApiExceptions
import repository.MarksRepository
import store.marks.MarksStore.*
import store.marks.MarksStoreAbstractFactory

internal class MarksStoreFactory(
  storeFactory: StoreFactory,
  val repository: MarksRepository,
  val researchId: Int
) : MarksStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      singleFromCoroutine {
        repository.getMarks()
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(isThreadLocal = true, onSuccess = ::dispatch)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleNewMark -> TODO()
        Intent.DismissError -> TODO()
        Intent.ReloadRequested -> TODO()
      }.let {}
    }

    private fun handleError(error: Throwable) {
      val result = when (error) {
        is ResearchApiExceptions.MarksFetchException -> Result.Error(error.error)
        is ResearchApiExceptions.ResearchNotFoundException -> Result.Error(error.error)
        else -> {
          println("marks: other exception ${error.message}")
          Result.Error(MARKS_FETCH_EXCEPTION)
        }
      }
      dispatch(result)
    }
  }

}
