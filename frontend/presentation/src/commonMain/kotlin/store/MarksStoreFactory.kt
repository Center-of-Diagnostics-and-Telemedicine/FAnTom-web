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
        is Intent.UnselectMark -> unselectMark(intent.mark, getState)
        is Intent.UpdateMark -> updateMarkWithoutSaving(intent.markToUpdate, getState)
        is Intent.UpdateMarkWithSave -> updateMarkWithSave(intent.mark)
        Intent.ReloadRequested -> TODO()
        Intent.DismissError -> TODO()
      }.let {}
    }

    private fun updateMarkWithSave(mark: MarkDomain) {
      singleFromCoroutine {
        repository.updateMark(mark)
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

    private fun updateMarkWithoutSaving(markToUpdate: MarkDomain, getState: () -> State) {
      val marks = getState().marks.replace(markToUpdate) { it.id == markToUpdate.id }
      dispatch(Result.Loaded(marks))
      publish(Label.MarksLoaded(marks))
    }

    private fun selectMark(mark: MarkDomain, state: () -> State) {
      val marks = state().marks
      marks.firstOrNull { it.id == mark.id }?.let { it.selected = true }
      dispatch(Result.Loaded(marks))
      publish(Label.MarksLoaded(marks))
    }


    private fun unselectMark(mark: MarkDomain, state: () -> State) {
      val marks = state().marks
      marks.firstOrNull { it.id == mark.id }?.let { it.selected = false }
      dispatch(Result.Loaded(marks))
      publish(Label.MarksLoaded(marks))
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

fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean): List<T> {
  return map {
    if (block(it)) newValue else it
  }
}
