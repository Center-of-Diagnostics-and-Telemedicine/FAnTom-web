package research.cut

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.MCircularProgressColor
import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.size
import components.alert
import controller.CutController
import controller.CutControllerImpl
import destroy
import kotlinx.css.*
import model.Cut
import model.Research
import react.*
import repository.BrightnessRepository
import repository.MipRepository
import repository.ResearchRepository
import research.cut.draw.DrawViewProxy
import research.cut.draw.drawView
import research.cut.shapes.ShapesViewProxy
import research.cut.shapes.shapesView
import resume
import styled.css
import styled.styledDiv
import view.*

class CutParentComponent(prps: CutParentProps) : RComponent<CutParentProps, CutParentState>(prps) {

  private val cutViewDelegate = CutViewProxy(::updateState)
  private val shapesViewDelegate = ShapesViewProxy(::updateState)
  private val drawViewDelegate = DrawViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: CutController

  init {
    state = CutParentState(
      cutModel = initialCutModel(),
      shapesModel = initialShapesModel(),
      drawModel = initialDrawModel()
    )
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(
      cutView = cutViewDelegate,
      shapesView = shapesViewDelegate,
      drawView = drawViewDelegate,
      viewLifecycle = lifecycleRegistry
    )
    val dependencies = props.dependencies
    val disposable = dependencies.cutsInput.subscribe { controller.input(it) }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
  }

  private fun createController(): CutController {
    val dependencies = props.dependencies
    val cutControllerDependencies =
      object : CutController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return CutControllerImpl(cutControllerDependencies)
  }

  override fun RBuilder.render() {
    alert(
      message = state.cutModel.error,
      open = state.cutModel.error.isNotEmpty(),
      handleClose = { cutViewDelegate.dispatch(CutView.Event.DismissError) }
    )
    styledDiv {
      css {
        position = Position.absolute
        top = 0.px
        left = 0.px
        display = Display.flex
      }
      val containerWidth = props.dependencies.width
      val containerHeight = props.dependencies.height
      if (state.cutModel.mainLoading) {
        cutLoading(
          w = containerWidth,
          h = containerHeight,
        )
      } else {
        cutView(
          w = containerWidth,
          h = containerHeight,
          slice = state.cutModel.slice,
          reversed = props.dependencies.cut.data.reversed,
          loading = state.cutModel.mainLoading
        )
      }
      shapesView(
        cut = props.dependencies.cut,
        width = containerWidth,
        height = containerHeight,
        shapesModel = state.shapesModel,
        eventOutput = { shapesViewDelegate.dispatch(it) },
        loading = state.cutModel.mainLoading
      )
      drawView(
        cut = props.dependencies.cut,
        width = containerWidth,
        height = containerHeight,
        drawModel = state.drawModel,
        eventOutput = { drawViewDelegate.dispatch(it) }
      )
    }
  }

  private fun RBuilder.cutLoading(h: Int, w: Int) {
    mCircularProgress(color = MCircularProgressColor.secondary) {
      attrs.size = 50.px
      css {
        position = Position.absolute
        top = (h / 2).px
        left = (w / 2).px
        marginTop = (-25).px
        marginLeft = (-25).px
      }
    }
  }

  private fun updateState(model: CutView.Model) = setState { cutModel = model }
  private fun updateState(model: ShapesView.Model) = setState { shapesModel = model }
  private fun updateState(model: DrawView.Model) = setState { drawModel = model }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Cut
    val cutsInput: Observable<CutController.Input>
    val cutOutput: (CutController.Output) -> Unit
    val researchRepository: ResearchRepository
    val brightnessRepository: BrightnessRepository
    val mipRepository: MipRepository
    val research: Research
    val height: Int
    val width: Int
  }

}

class CutParentState(
  var cutModel: CutView.Model,
  var shapesModel: ShapesView.Model,
  var drawModel: DrawView.Model
) : RState

interface CutParentProps : RProps {
  var dependencies: CutParentComponent.Dependencies
}

fun RBuilder.cut(
  dependencies: CutParentComponent.Dependencies
) = child(CutParentComponent::class) {
  attrs.dependencies = dependencies
}
