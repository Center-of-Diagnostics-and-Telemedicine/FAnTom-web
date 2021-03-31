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
import repository.ExpertMarksRepository
import repository.ExpertRoiRepository
import store.expert.ExpertMarksStore.*
import store.expert.ExpertMarksStoreAbstractFactory

internal class ExpertMarksStoreFactory(
  storeFactory: StoreFactory,
  val research: Research,
  val data: ResearchSlicesSizesDataNew,
  val expertRoiRepository: ExpertRoiRepository,
  val expertMarksRepository: ExpertMarksRepository
) : ExpertMarksStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      singleFromCoroutine {
        val rois = expertRoiRepository.getRois(research.id)
        val expertMarks = expertMarksRepository.getMarks(research.id)
        buildRoisToExpertMarks(rois, expertMarks, data.markTypes)
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = {
            dispatch(it)
            publish(Label.Marks(it.models))
          },
          onError = ::handleError
        )
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeVariant -> handleChangeVariant(getState, intent.question, intent.roi)
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
        is Intent.ChangeVariantText -> TODO()
        Intent.ReloadRequested -> null
        Intent.HandleCloseResearch -> handleCloseResearch(getState)
        is Intent.SelectMark -> handleSelectMark(getState, intent.id)
      }
    }

    private fun handleSelectMark(state: () -> State, id: Int) {
      val list = state().models
      val mark = list.firstOrNull { it.roiModel.id == id }
      val markSelected = mark?.selected
      list.map { it.selected = false }
      mark?.let { it.selected = markSelected?.not() ?: true }
      dispatch(Result.Loaded(list))
      publish(Label.Marks(list))
    }

    private fun handleCloseResearch(getState: () -> State) {
//      val mark = getState().covidLungLobes
//      val modelWithNoValue = mark.values.firstOrNull { it.value == null }
//      if (modelWithNoValue != null) {
//        dispatch(Result.Error(COVID_MARK_TYPE_NOT_SET))
//      } else {
//        publish(Label.CloseResearch)
//      }
    }

    private fun handleChangeVariant(
      getState: () -> State,
      question: ExpertQuestion<*>,
      roi: ExpertRoiEntity
    ) {

//      singleFromCoroutine {
//        val newModel = lungLobeModel.changeValue(variant)
//        val newMap = getState().covidLungLobes.toMutableMap()
//        newMap[lungLobeModel.id] = newModel
//        repository.saveMark(newMap.toExpertMarkEntity(), research.id)
//        return@singleFromCoroutine newMap
//      }
//        .subscribeOn(ioScheduler)
//        .map(Result::Loaded)
//        .observeOn(mainScheduler)
//        .subscribeScoped(
//          isThreadLocal = true,
//          onSuccess = ::dispatch,
//          onError = ::handleError
//        )
    }

    private fun handleError(error: Throwable) {
      val result = when (error) {
        is ResearchApiExceptions -> Result.Error(error.error)
        else -> {
          println("cut: other exception ${error.message}")
          Result.Error(BASE_ERROR)
        }
      }
      dispatch(result)
    }
  }

}
