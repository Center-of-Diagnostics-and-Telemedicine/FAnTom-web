package components.listroot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.list.ResearchList
import components.listfilters.ListFilters

internal class ListRootComponent(
  componentContext: ComponentContext,
  private val filter: (ComponentContext, Consumer<ListFilters.Output>) -> ListFilters,
  private val list: (ComponentContext, Consumer<ResearchList.Output>) -> ResearchList,
) : ListRoot, ComponentContext by componentContext {

  private val filtersRouter =
    router(
      initialConfiguration = Configuration.Filters,
      key = "FiltersRouter",
      childFactory = { conf, ctx -> ListRoot.Filters(filter(ctx, Consumer(::onFilterOutput))) }
    )

  override val filtersRouterState: Value<RouterState<*, ListRoot.Filters>> = filtersRouter.state

  private val listRouter =
    router(
      initialConfiguration = Configuration.List,
      key = "ListRouter",
      childFactory = { conf, ctx -> ListRoot.List(list(ctx, Consumer(::onEditOutput))) }
    )

  override val listRouterState: Value<RouterState<*, ListRoot.List>> = listRouter.state

  private fun onFilterOutput(any: Any) {
    TODO("Not yet implemented")
  }

  private fun onEditOutput(any: Any) {
    TODO("Not yet implemented")
  }

  private sealed class Configuration : Parcelable {
    //    @Parcelize
    object Filters : Configuration()

    //    @Parcelize
    object List : Configuration()
  }
}