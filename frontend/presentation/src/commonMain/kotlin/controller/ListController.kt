package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import mapper.*
import store.FilterStoreFactory
import store.ListStoreFactory
import view.FilterView
import view.ListView

class ListControllerImpl(dependencies: ListController.Dependencies) : ListController {

  private val listStore =
    ListStoreFactory(
      storeFactory = dependencies.storeFactory,
      repository = dependencies.researchRepository
    ).create()

  private val filterStore =
    FilterStoreFactory(
      storeFactory = dependencies.storeFactory
    ).create()

  init {
    dependencies.lifecycle.doOnDestroy {
      listStore.dispose()
      filterStore.dispose()
    }
  }

  override fun onViewCreated(
    listView: ListView,
    filterView: FilterView,
    viewLifecycle: Lifecycle,
    output: (ListController.Output) -> Unit
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      listView.events.mapNotNull(listEventToListIntent) bindTo listStore
      filterView.events.mapNotNull(addEventToAddIntent) bindTo filterStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      listStore.states.mapNotNull(listStateToListModel) bindTo listView
      filterStore.states.mapNotNull(filterStateToFilterModel) bindTo filterView
      listView.events.mapNotNull(listEventToOutput) bindTo output
    }
  }
}
