package components.cutscontainer

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.doOnAfterNext
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribeOn
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.GridModel
import model.GridType
import model.ResearchDataModel
import model.buildModel
import repository.GridRepository
import repository.MyResearchRepository
import store.gridcontainer.MyCutsContainerStore
import store.gridcontainer.MyCutsContainerStore.*

internal class CutsContainerStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchRepository: MyResearchRepository,
  private val gridRepository: GridRepository,
  private val researchId: Int,
  private val data: ResearchDataModel,
) {

  fun provide(): MyCutsContainerStore =
    object : MyCutsContainerStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CutsContainerStore_$researchId",
      initialState = State(
        gridType = GridType.initial,
        gridModel = GridType.initial.buildModel(data),
      ),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class GridChanged(val grid: GridType) : Result()
    data class GridModelChanged(val gridModel: GridModel) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {
    override fun executeAction(action: Unit, getState: () -> State) {
      gridRepository
        .grid
        .map(Result::GridChanged)
        .doOnAfterNext { publish(Label.GridTypeChanged(it.grid)) }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(onNext = ::dispatch)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        else -> throw NotImplementedError("not implemented for $intent")
      }.let {}
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.GridChanged -> copy(gridType = result.grid)
        is Result.GridModelChanged -> copy(gridModel = result.gridModel)
      }
  }
}