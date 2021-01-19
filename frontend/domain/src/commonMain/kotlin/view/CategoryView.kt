package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Category
import view.CategoryView.Event
import view.CategoryView.Model

interface CategoryView : MviView<Model, Event> {

  data class Model(
    val items: List<Category>,
    val current: Category
  )

  sealed class Event {
    data class ItemClick(val category: Category): Event()
  }
}

fun initialCategoryModel(): Model = Model(
  items = listOf(),
  current = Category.All
)