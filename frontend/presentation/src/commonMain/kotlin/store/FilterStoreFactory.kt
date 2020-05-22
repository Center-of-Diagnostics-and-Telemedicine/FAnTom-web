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
import store.FilterStore.*

internal class FilterStoreFactory(
  storeFactory: StoreFactory
) : FilterStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor():
    Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Unit, State, Result, Label>() {
    override fun executeAction(action: Unit, getState: () -> State) {
      singleFromCoroutine {
        listOf(Filter.All, Filter.NotSeen, Filter.Done, Filter.Seen)
      }
        .subscribeOn(ioScheduler)
        .map { Result.Loaded(it) }
        .observeOn(mainScheduler)
        .subscribeScoped(isThreadLocal = true, onSuccess = ::dispatch)
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleFilterClick -> changeFilter(intent.filter)
      }.let {}
    }

    private fun changeFilter(filter: Filter) {
      dispatch(Result.FilterChanged(filter))
      publish(Label.FilterChanged(filter))
    }
  }
}