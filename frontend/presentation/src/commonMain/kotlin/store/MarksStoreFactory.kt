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
import model.*
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
        repository.getMarks(researchId)
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = {
            dispatch(it)
            publish(Label.MarksLoaded(it.marks))
          },
          onError = ::handleError
        )
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleNewMark -> handleNewMark(intent.circle, intent.sliceNumber, intent.cut)
        is Intent.SelectMark -> selectMark(intent.mark, getState)
        Intent.DismissError -> TODO()
        Intent.ReloadRequested -> TODO()
      }.let {}
    }

    private fun selectMark(mark: MarkDomain, state: () -> State) {
      val marks = state().marks
      marks.firstOrNull { it.id == mark.id }?.let { it.selected = true }
      dispatch(Result.Loaded(marks))
      publish(Label.MarksLoaded(marks))
    }

    private fun handleNewMark(circle: Circle, sliceNumber: Int, cut: Cut) {
      singleFromCoroutine {
        val markToSave = cut.getMarkToSave(circle, sliceNumber)
        repository.saveMark(markToSave!!, researchId)
        repository.getMarks(researchId)
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = {
            dispatch(it)
            publish(Label.MarksLoaded(it.marks))
          },
          onError = ::handleError
        )
    }

    private fun handleError(error: Throwable) {
      println("MarksStore" + error.message)
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
