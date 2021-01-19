package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.LungLobeModel
import model.ResearchSlicesSizesDataNew
import model.changeValue
import replace
import repository.MarksRepository
import store.covid.CovidMarksStore.*
import store.covid.CovidMarksStoreAbstractFactory

internal class CovidMarksStoreFactory(
  storeFactory: StoreFactory,
  val repository: MarksRepository,
  val researchId: Int,
  val data: ResearchSlicesSizesDataNew
) : CovidMarksStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
//      singleFromCoroutine {
//        repository.getCovidMark(researchId)
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

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeVariant -> handleChangeVariant(
          intent.lungLobeModel,
          intent.variant,
          getState
        )
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
        Intent.ReloadRequested -> TODO()
        Intent.DeleteClicked -> TODO()
        Intent.HandleCloseResearch -> TODO()
      }.let {}
    }

    private fun handleChangeVariant(
      lungLobeModel: LungLobeModel,
      variant: Int,
      getState: () -> State
    ) {
      val newModel = lungLobeModel.changeValue(variant)
      val newList = getState()
        .covidLungLobes
        .toMutableList()
        .replace(newModel) { it.shortName == lungLobeModel.shortName }
      dispatch(Result.Loaded(newList))
    }
  }

}