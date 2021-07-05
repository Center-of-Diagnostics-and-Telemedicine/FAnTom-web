package components.researchmarks

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import com.badoo.reaktive.utils.ensureNeverFrozen
import com.badoo.reaktive.utils.printStack
import model.*
import repository.MarksRepository
import repository.ResearchRepository
import store.marks.MarksStore
import store.marks.MarksStore.*

internal class ResearchMarksStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchRepository: ResearchRepository,
  private val marksRepository: MarksRepository,
  private val researchId: Int
) {

  fun provide(): MarksStore =
    object : MarksStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "ResearchMarksStore",
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
    data class Loaded(val marks: List<MarkModel>) : Result()
    data class MarkTypesLoaded(val markTypes: List<MarkTypeModel>) : Result()
    data class Error(val error: String) : Result()
    object DismissErrorRequested : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
//      val markTypes = data.markTypes.map { it.value.toMarkTypeModel(it.key) }
//      dispatch(Result.MarkTypesLoaded(markTypes))
//
//      singleFromCoroutine {
//        researchRepository.getMarks(research.id)
//      }
//        .map { list -> list.map { it.toMarkModel(data.markTypes) } }
//        .subscribeSingle()
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleNewMark ->
          handleNewMark(intent.shape, intent.sliceNumber, intent.cut, getState)
        is Intent.SelectMark -> selectMark(intent.mark, getState)
        is Intent.UnselectMark -> unSelectMark(intent.mark, getState)
        is Intent.UpdateMarkWithoutSaving -> updateMarkWithoutSaving(intent.markToUpdate, getState)
        is Intent.UpdateMarkWithSave -> updateMarkWithSave(intent.mark, getState)
        is Intent.DeleteMark -> deleteMark(intent.mark, getState)
        is Intent.UpdateComment -> updateComment(intent.mark, intent.comment, getState)
        Intent.DeleteClicked ->
          getState().marks.firstOrNull { it.selected }?.let { deleteMark(it, getState) }
        is Intent.ChangeMarkType ->
          getState().marks.firstOrNull { it.id == intent.markId }?.copy(type = intent.type)?.let {
            updateMarkWithSave(it, getState)
          }
        Intent.HandleCloseResearch -> handleCloseResearch(getState)
        Intent.ReloadRequested -> TODO()
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
        is Intent.ChangeVisibility -> handleChangeVisibility(intent.mark, getState)
        is Intent.AcceptMark -> handleAcceptMark(intent.mark, getState)
      }.let {}
    }

    private fun handleAcceptMark(mark: MarkModel, state: () -> State) {
      if (mark.type == null) {
        dispatch(Result.Error(error = "Выберите тип отметки"))
      } else {
        deleteMark(mark, state)
        publish(Label.AcceptMark(mark))
      }
    }

    private fun handleChangeVisibility(mark: MarkModel, state: () -> State) {
//      val result = state().marks.toList()
//      result.find { it.id == mark.id }?.let { it.visible = !it.visible }
//      dispatch(Result.Loaded(result))
//      publish(Label.MarksLoaded(result))
    }

    private fun handleNewMark(shape: Shape, sliceNumber: Int, cut: Cut, getState: () -> State) {
//      singleFromCoroutine {
//        val markToSave = cut.getMarkToSave(shape, sliceNumber)
//        val mark = researchRepository.saveMark(markToSave!!, research.id).toMarkModel(data.markTypes)
//        getState().marks.plus(mark)
//      }.subscribeSingle()
    }

    private fun updateMarkWithSave(mark: MarkModel, getState: () -> State) {
//      singleFromCoroutine {
//        researchRepository.updateMark(mark.toMarkEntity(), research.id)
//        getState().marks.replace(newValue = mark, block = { it.id == mark.id })
//      }.subscribeSingle()
    }

    private fun deleteMark(mark: MarkModel, getState: () -> State) {
//      singleFromCoroutine {
//        researchRepository.deleteMark(mark.id, research.id)
//        getState().marks.filter { it.id != mark.id }.minus(mark)
//      }.subscribeSingle()
    }

    private fun updateComment(mark: MarkModel, comment: String, getState: () -> State) {
//      singleFromCoroutine {
//        val updatedMarkEntity = mark.toMarkEntity().copy(comment = comment)
//        val updatedMarkModel = updatedMarkEntity.toMarkModel(data.markTypes)
//        researchRepository.updateMark(updatedMarkEntity, research.id)
//        getState().marks.replace(updatedMarkModel) { it.id == mark.id }
//      }.subscribeSingle()
    }

    private fun handleCloseResearch(state: () -> State) {
//      val markWithoutType = state().marks.firstOrNull { it.type == null }
//      if (markWithoutType != null) {
//        dispatch(Result.Error(MARK_TYPE_NOT_SET))
//      } else {
//        publish(Label.CloseResearch)
//      }
    }

    private fun updateMarkWithoutSaving(mark: MarkModel, getState: () -> State) {
//      val marks = getState().marks.replace(newValue = mark, block = { it.id == mark.id })
//      dispatch(Result.Loaded(marks))
//      publish(Label.MarksLoaded(marks))
    }

    private fun selectMark(mark: MarkModel, state: () -> State) {
//      singleFromCoroutine {
//        val list = state().marks
//        list.map { it.selected = false }
//        list.firstOrNull { it.id == mark.id }?.selected = true
//        if (research.isPlanar().not()) publish(Label.CenterSelectedMark(mark))
//        list
//      }.subscribeSingle()
    }

    private fun unSelectMark(mark: MarkModel, state: () -> State) {
//      singleFromCoroutine {
//        val list = state().marks
//        list.map { it.selected = false }
//        list
//      }.subscribeSingle()
    }

    private fun Single<List<MarkModel>>.subscribeSingle() =
      subscribeOn(ioScheduler)
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

    private fun handleError(error: Throwable) {
      error.printStack()
      println("MarksStore" + error.message)
      val result = when (error) {
        is ResearchApiExceptions -> Result.Error(error.error)
        else -> {
          println("marks: other exception ${error.message}")
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
        is Result.Loaded -> copy(marks = result.marks, loading = false)
        is Result.MarkTypesLoaded -> copy(markTypes = result.markTypes, loading = false)
        is Result.Error -> copy(error = result.error, loading = false)
        is Result.DismissErrorRequested -> copy(error = "")
      }
  }
}