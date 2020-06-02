package research.gridcontainer

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.publish.PublishSubject
import controller.*
import destroy
import kotlinx.css.*
import model.Cut
import model.Grid
import model.ResearchSlicesSizesData
import react.*
import repository.ResearchRepository
import research.cut.CutContainer
import research.cut.cutContainer
import research.gridcontainer.GridContainerViewComponent.GridContainerStyles.columnOfRowsStyle
import research.gridcontainer.GridContainerViewComponent.GridContainerStyles.rowOfColumnsStyle
import resume
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.GridContainerView.Model
import view.initialGridContainerModel

class GridContainerViewComponent(prps: GridContainerProps) :
  RComponent<GridContainerProps, GridContainerState>(prps) {

  private val gridViewDelegate = GridContainerViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: GridContainerController
  private val shapesInputObservable = PublishSubject<ShapesController.Input>()
  private val disposable = CompositeDisposable()

  init {
    state = GridContainerState(initialGridContainerModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    val dependencies = props.dependencies
    disposable.add(dependencies.gridContainerInputs.subscribe { controller.input(it) })
    controller.onViewCreated(gridViewDelegate, lifecycleRegistry)
  }

  private fun createController(): GridContainerController {
    val dependencies = props.dependencies
    val researchControllerDependencies =
      object : GridContainerController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return GridContainerControllerImpl(researchControllerDependencies)
  }

  override fun RBuilder.render() {
    val cutsModel = state.cutsModel
    if (cutsModel.items.isNotEmpty()) {
      styledDiv {
        css(columnOfRowsStyle)
        when (cutsModel.grid) {
          is Grid.Single -> singleCutContainer(cutsModel.items.first())
          is Grid.TwoVertical -> twoVerticalCutsContainer(cutsModel.items)
          is Grid.TwoHorizontal -> twoHorizontalCutsContainer(cutsModel.items)
          is Grid.Four -> fourCutsContainer(cutsModel.items)
        }
      }
    }
  }

  private fun RBuilder.singleCutContainer(item: Cut) {
    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(item))
    }
  }

  private fun RBuilder.twoHorizontalCutsContainer(items: List<Cut>) {
    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(items.first()))
      cutContainer(dependencies = dependencies(items.last()))
    }
  }

  private fun RBuilder.twoVerticalCutsContainer(items: List<Cut>) {
    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(items.first()))
    }

    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(items.last()))
    }
  }

  private fun RBuilder.fourCutsContainer(items: List<Cut>) {
    val leftTop = items[0]
    val rightTop = items[1]
    val leftBottom = items[2]
    val rightBottom = items[3]

    //это колонка строк срезов(верх низ)
    styledDiv {
      css(columnOfRowsStyle)

      //одна из строк (лево/право)
      styledDiv {
        css(rowOfColumnsStyle)

        //контейнер для среза
        cutContainer(
          dependencies = dependencies(leftTop)
        )
        //TODO() пока известно что сверху справа пустой, поэтому заглушка
        styledDiv {
          css {
            display = Display.flex
            flexDirection = FlexDirection.row
            position = Position.relative
            width = 50.pct
          }
        }

      }

      styledDiv {
        css(rowOfColumnsStyle)

        cutContainer(dependencies = dependencies(leftBottom))
        cutContainer(dependencies = dependencies(rightBottom))
      }
    }
  }

  private fun dependencies(cut: Cut): CutContainer.Dependencies =
    object : CutContainer.Dependencies, Dependencies by props.dependencies {
      override val cut: Cut = cut
      override val cutOutput: (CutController.Output) -> Unit = ::cutOutput
      override val shapesOutput: (ShapesController.Output) -> Unit = ::shapesOutput
      override val drawOutput: (DrawController.Output) -> Unit = ::drawOutput
      override val shapesInput: Observable<ShapesController.Input> = this@GridContainerViewComponent.shapesInputObservable
    }

  private fun cutOutput(output: CutController.Output) {
    when (output) {
      is CutController.Output.SliceNumberChanged -> shapesInputObservable.onNext(
        ShapesController.Input.ExternalSliceNumberChanged(output.sliceNumber, output.cut)
      )
//      is CutController.Output.BrightnessChanged -> TODO()
    }
  }

  private fun shapesOutput(output: ShapesController.Output) {
//    when (output) {
//      is CutController.Output.SliceNumberChanged -> TODO()
//      is CutController.Output.BrightnessChanged -> TODO()
//    }
  }

  private fun drawOutput(output: DrawController.Output) {
    when (output) {
      is DrawController.Output.OnClick -> TODO()
      is DrawController.Output.ChangeContrastBrightness -> TODO()
      is DrawController.Output.Drawing ->
        shapesInputObservable.onNext(ShapesController.Input.Drawing(output.circle, output.cutType))
      is DrawController.Output.StartMoving -> TODO()
      is DrawController.Output.MousePosition ->
        shapesInputObservable.onNext(ShapesController.Input.MousePosition(output.dicomX, output.dicomY, output.cutType))
      is DrawController.Output.Drawn -> TODO()
      is DrawController.Output.ChangeSlice -> TODO()
    }
  }

  private fun updateState(model: Model) = setState { cutsModel = model }

  override fun componentWillUnmount() {
    disposable.dispose()
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val data: ResearchSlicesSizesData
    val gridContainerInputs: Observable<GridContainerController.Input>
    val cutsInput: Observable<CutController.Input>
    val researchRepository: ResearchRepository
    val researchId: Int
  }

  object GridContainerStyles : StyleSheet("CutsStyles", isStatic = true) {

    val columnOfRowsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.column
    }

    val rowOfColumnsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
    }
  }
}

class GridContainerState(
  var cutsModel: Model
) : RState

interface GridContainerProps : RProps {
  var dependencies: GridContainerViewComponent.Dependencies
}

fun RBuilder.cuts(dependencies: GridContainerViewComponent.Dependencies) = child(
  GridContainerViewComponent::class
) {
  attrs.dependencies = dependencies
}
