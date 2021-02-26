package mapper

import store.list.FilterStore.Intent
import store.list.FilterStore.State
import view.CategoryView
import view.FilterView.Event
import view.FilterView.Model

val filterStateToFilterModel: State.() -> Model? = {
  Model(
    items = filters,
    current = currentFilter,
  )
}

val filterEventToFilterIntent: Event.() -> Intent? =
  {
    when (this) {
      is Event.ItemClick -> Intent.HandleFilterClick(filter)
    }
  }

val filterStateToCategoryModel: State.() -> CategoryView.Model? = {
  CategoryView.Model(
    items = categories,
    current = currentCategory
  )
}

val categoryEventToFilterIntent: CategoryView.Event.() -> Intent? =
  {
    when(this){
      is CategoryView.Event.ItemClick -> Intent.HandleCategoryClick(category)
    }
  }
