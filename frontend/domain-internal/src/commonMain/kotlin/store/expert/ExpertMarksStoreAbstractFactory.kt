package store.expert

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.ExpertQuestion
import model.MarkModel
import model.RoiExpertQuestionsModel
import model.expertQuestionsList
import store.expert.ExpertMarksStore.*

abstract class ExpertMarksStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): ExpertMarksStore =
    object : ExpertMarksStore, Store<Intent, State, Label> by storeFactory.create(
      name = "ExpertMarksStore",
      initialState = State(),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Unit, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    object Loading : Result()
    data class Loaded(val questions: List<RoiExpertQuestionsModel>) : Result()
//    data class ChangeCurrentMark(val markModel: MarkModel) : Result()
//    data class UpdateQuestions(
//      val markModel: MarkModel,
//      val questionsAnswers: List<ExpertQuestion<*>>
//    ) : Result()

    data class Error(val error: String) : Result()
    object DismissErrorRequested : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.Loaded -> copy(roisQuestions = result.questions)
//        is Result.ChangeCurrentMark -> copy(current = result.markModel to expertQuestionsList)
//        is Result.UpdateQuestions -> copy(current = result.markModel to result.questionsAnswers)
        is Result.DismissErrorRequested -> copy(error = "")
        is Result.Error -> copy(error = result.error, loading = false)
      }
  }
}