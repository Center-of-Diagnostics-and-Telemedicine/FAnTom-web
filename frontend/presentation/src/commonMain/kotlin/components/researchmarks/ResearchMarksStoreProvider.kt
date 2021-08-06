package components.researchmarks

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.observeOn
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.coroutinesinterop.completableFromCoroutine
import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.utils.ensureNeverFrozen
import com.badoo.reaktive.utils.printStack
import model.*
import repository.MyMarksRepository
import repository.MyResearchRepository
import store.marks.MyMarksStore
import store.marks.MyMarksStore.*

internal class ResearchMarksStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchRepository: MyResearchRepository,
  private val marksRepository: MyMarksRepository,
  private val researchId: Int,
  private val data: ResearchDataModel
) {

  fun provide(): MyMarksStore =
    object : MyMarksStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "MyMarksStore",
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
      marksRepository
        .marks
        .notNull()
        .mapIterable { it.toMarkModel(data.markTypes) }
        .map(Result::Loaded)
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(onNext = ::dispatch, onError = ::handleError)

      marksRepository
        .mark
        .map { it?.toMarkModel(data.markTypes) }
        .map(Result::CurrentMark)
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(onNext = ::dispatch, onError = ::handleError)

      val markTypes = data.markTypes.map { it.value.toMarkTypeModel(it.key) }
      dispatch(Result.MarkTypesLoaded(markTypes))

      load()
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.SelectMark -> handleSelectMark(intent.id, getState())
        is Intent.DeleteMark -> handleDeleteMark(intent.id, getState())
        is Intent.ChangeComment -> handleChangeComment(intent.id, intent.comment, getState())
        is Intent.ChangeMarkType -> handleChangeMarkType(intent.id, intent.typeId, getState())
        is Intent.ChangeVisibility -> handleChangeVisibility(intent.id, getState())
        Intent.ReloadRequested -> load()
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
      }.let {}
    }

    private fun handleSelectMark(id: Int, state: State) {
      completableFromCoroutine {
        state.marks.firstOrNull { it.id == id }?.let { mark ->
          marksRepository.setMark(mark.toMarkEntity())
        }
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped()
    }

    fun handleDeleteMark(id: Int, state: State) {
      completableFromCoroutine {
        marksRepository.deleteMark(id, researchId)
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped()
    }

    fun handleChangeComment(id: Int, comment: String, state: State) {
      completableFromCoroutine {
        state.currentMark?.let { mark ->
          marksRepository.updateMark(mark.copy(comment = comment).toMarkEntity(), researchId)
        }
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped()
    }

    fun handleChangeMarkType(id: Int, typeId: String, state: State) {
      completableFromCoroutine {
        state.currentMark?.let { mark ->
          val markType = state.markTypes.first { it.typeId == typeId }
          marksRepository.updateMark(mark.copy(type = markType).toMarkEntity(), researchId)
        }
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped()
    }

    fun handleChangeVisibility(id: Int, state: State) {
//      completableFromCoroutine {
//        state.marks.firstOrNull { it.id == id }.let { mark ->
//          marksRepository.updateMark(mark.copy(type = markType).toMarkEntity(), researchId)
//        }
//      }
//        .subscribeOn(ioScheduler)
//        .observeOn(mainScheduler)
//        .subscribeScoped()
    }

    private fun load() {
      completableFromCoroutine {
        marksRepository.loadMarks(researchId)
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(onError = ::handleError)
    }

    private fun handleError(error: Throwable) {
      error.printStack()
      println("MyMarksStore" + error.message)
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

  private sealed class Result : JvmSerializable {
    object Loading : Result()
    data class Loaded(val marks: List<MarkModel>) : Result()
    data class CurrentMark(val mark: MarkModel?) : Result()
    data class MarkTypesLoaded(val markTypes: List<MarkTypeModel>) : Result()
    data class Error(val error: String) : Result()
    object DismissErrorRequested : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.Loaded -> copy(marks = result.marks, loading = false)
        is Result.CurrentMark -> copy(currentMark = result.mark)
        is Result.MarkTypesLoaded -> copy(markTypes = result.markTypes, loading = false)
        is Result.Error -> copy(error = result.error, loading = false)
        is Result.DismissErrorRequested -> copy(error = "")
      }
  }
}