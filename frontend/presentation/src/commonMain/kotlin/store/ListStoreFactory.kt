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
import model.Filter
import repository.ResearchRepository
import store.ListStore.Intent
import store.ListStore.State

internal class ListStoreFactory(
  storeFactory: StoreFactory,
  private val repository: ResearchRepository
) : ListStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
    override fun executeAction(action: Unit, getState: () -> State) = load()

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleFilterChanged -> filterChanged(intent.filter)
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
        Intent.ReloadRequested -> load()
      }.let {}
    }

    private fun filterChanged(filter: Filter) {
      singleFromCoroutine {
        repository.getFiltered(filter)
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(isThreadLocal = true, onSuccess = ::dispatch)
    }

    private fun load() {
      singleFromCoroutine {
        repository.getResearches()
      }
        .subscribeOn(ioScheduler)
        .map(Result::Loaded)
        .observeOn(mainScheduler)
        .subscribeScoped(isThreadLocal = true, onSuccess = ::dispatch)
    }
  }
}