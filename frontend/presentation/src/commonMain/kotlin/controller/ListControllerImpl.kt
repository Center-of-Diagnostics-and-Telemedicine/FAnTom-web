package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import mapper.*
import store.FilterStoreFactory
import store.ListStoreFactory
import view.CategoryView
import view.FilterView
import view.ListView

class ListControllerImpl(val dependencies: ListController.Dependencies) : ListController {

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
    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      filterStore.labels.mapNotNull(filterLabelToListIntent) bindTo listStore
    }

    dependencies.lifecycle.doOnDestroy {
      listStore.dispose()
      filterStore.dispose()
    }
  }

  override fun onViewCreated(
    listView: ListView,
    filterView: FilterView,
    categoryView: CategoryView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      listView.events.mapNotNull(listEventToListIntent) bindTo listStore
      filterView.events.mapNotNull(filterEventToFilterIntent) bindTo filterStore
      categoryView.events.mapNotNull(categoryEventToFilterIntent) bindTo filterStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      listStore.states.mapNotNull(listStateToListModel) bindTo listView
      filterStore.states.mapNotNull(filterStateToFilterModel) bindTo filterView
      filterStore.states.mapNotNull(filterStateToCategoryModel) bindTo categoryView
      listView.events.mapNotNull(listEventToOutput) bindTo dependencies.listOutput
    }
  }
}
