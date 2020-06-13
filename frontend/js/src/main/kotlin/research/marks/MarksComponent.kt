package research.marks

import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import controller.ToolsController
import react.*
import repository.ResearchRepository
import research.tools.ToolsViewProxy
import view.MarksView.Event
import view.MarksView.Model

class MarksComponent(prps: MarksProps) : RComponent<MarksProps, MarksState>(prps) {

  private val marksViewDelegate = MarksViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ToolsController

  override fun RBuilder.render() {

  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val marksOutput: (MarksController.Output) -> Unit
    val marksInput: Observable<MarksController.Input>
    val researchRepository: ResearchRepository
    val researchId: Int
  }

  fun updateState(marksModel: Model) = setState { model = marksModel }

  override fun componentWillUnmount() = 

}

class MarksState(var model: Model) : RState

interface MarksProps : RProps

fun RBuilder.marks(dependencies: MarksComponent.Dependencies) = child(MarksComponent::class) {
  attrs.dependencies = dependencies
}
