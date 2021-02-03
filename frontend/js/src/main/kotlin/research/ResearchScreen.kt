package research

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.dialog.*
import com.ccfraser.muirwik.components.form.MFormControlMargin
import components.alert
import components.screenLoading
import controller.*
import controller.ToolsController.Output
import destroy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import model.*
import org.w3c.dom.events.KeyboardEvent
import react.*
import repository.BrightnessRepository
import repository.MarksRepository
import repository.MipRepository
import repository.ResearchRepository
import research.ResearchScreen.ResearchStyles.appFrameContainerStyle
import research.covid.CovidMarksComponent
import research.covid.covidMarks
import research.gridcontainer.CutsContainerViewComponent
import research.gridcontainer.cuts
import research.marks.MarksComponent
import research.marks.marks
import research.tools.ToolsComponent
import research.tools.tools
import resume
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.ResearchView
import view.initialResearchModel
import kotlin.browser.window

class ResearchScreen(prps: ResearchProps) : RComponent<ResearchProps, ResearchState>(prps) {

  private val drawerWidth = 300

  private val researchViewDelegate = ResearchViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ResearchController

  private val cutsContainerInputObservable =
    BehaviorSubject<CutsContainerController.Input>(CutsContainerController.Input.Idle)
  private val cutsInputObservable = BehaviorSubject<CutController.Input>(CutController.Input.Idle)
  private val marksInputObservable = BehaviorSubject<MarksController.Input>(MarksController.Input.Idle)
  private val covidMarksInputObservable =
    BehaviorSubject<CovidMarksController.Input>(CovidMarksController.Input.Idle)
  private val toolsInputObservable = BehaviorSubject<ToolsController.Input>(ToolsController.Input.Idle)

  private var confirmationDialogValue: String = ""
  private var confirmationDialogSelectedValue: String = ""
  private var confirmationDialogOpen: Boolean = false
  private var confirmationDialogScrollOpen: Boolean = false

  init {
    state = ResearchState(
      toolsOpen = false,
      marksOpen = false,
      dialogOpen = false,
      researchModel = initialResearchModel()
    )
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(researchViewDelegate, lifecycleRegistry)
    window.addEventListener(type = "keydown", callback = {
      val keyboardEvent = it as KeyboardEvent
      if (keyboardEvent.keyCode == 46)
        marksInputObservable.onNext(MarksController.Input.DeleteClick)
    })
  }

  private fun createController(): ResearchController {
    val dependencies = props.dependencies
    val researchControllerDependencies =
      object : ResearchController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
        override val researchId: Int = props.dependencies.research.id
      }
    return ResearchControllerImpl(researchControllerDependencies)
  }

  override fun RBuilder.render() {
    mCssBaseline()
    val model = state.researchModel
    screenLoading(model.loading)
    alert(
      message = state.researchModel.error,
      open = state.researchModel.error.isNotEmpty(),
      handleClose = { researchViewDelegate.dispatch(ResearchView.Event.DismissError) }
    )

    formDialog(state.dialogOpen)

    styledDiv {
      css(appFrameContainerStyle)

      if (model.data != null) {

        when (props.dependencies.research.getCategoryByString()) {
          Category.Covid -> {
            covid(model)
          }
          else -> {
            markup(model)
          }
        }
      }
    }
  }

  private fun RBuilder.markup(model: ResearchView.Model) {
    //leftDrawer
    leftDrawer(
      open = state.toolsOpen,
      drawerWidth = if (state.toolsOpen) drawerWidth.px else 8.spacingUnits,
      onOpen = ::openTools,
      onClose = ::closeTools
    ) {
      tools(dependencies = object : ToolsComponent.Dependencies,
        Dependencies by props.dependencies {
        override val toolsOutput: (Output) -> Unit = ::toolsOutput
        override val toolsInput: Observable<ToolsController.Input> = this@ResearchScreen.toolsInputObservable
        override val open: Boolean = state.toolsOpen
        override val data: ResearchSlicesSizesDataNew = model.data!!
      })
    }

    mainContent(
      marginnLeft = if (state.toolsOpen) drawerWidth.px else 8.spacingUnits,
      marginnRight = if (state.marksOpen) drawerWidth.px else 16.spacingUnits
    ) {
      cuts(dependencies = object : CutsContainerViewComponent.Dependencies,
        Dependencies by props.dependencies {
        override val data: ResearchSlicesSizesDataNew = model.data!!
        override val cutsContainerInputs: Observable<CutsContainerController.Input> = this@ResearchScreen.cutsContainerInputObservable
        override val cutsContainerOutput: (CutsContainerController.Output) -> Unit = ::cutsContainerOutput
        override val cutsInput: Observable<CutController.Input> = this@ResearchScreen.cutsInputObservable
      })
    }

    rightDrawer(
      open = state.marksOpen,
      drawerWidth = if (state.marksOpen) drawerWidth.px else 8.spacingUnits,
      onOpen = ::openMarks,
      onClose = ::closeMarks
    ) {
      val sizesData = model.data
      marks(dependencies = object : MarksComponent.Dependencies,
        Dependencies by props.dependencies {
        override val marksOutput: (MarksController.Output) -> Unit = ::marksOutput
        override val marksInput: Observable<MarksController.Input> = this@ResearchScreen.marksInputObservable
        override val isPlanar: Boolean = sizesData!!.type.isPlanar()
        override val data: ResearchSlicesSizesDataNew = sizesData!!
      })
    }
  }

  private fun RBuilder.covid(model: ResearchView.Model) {
    //leftDrawer
    leftDrawer(
      open = state.toolsOpen,
      drawerWidth = if (state.toolsOpen) drawerWidth.px else 8.spacingUnits,
      onOpen = ::openTools,
      onClose = ::closeTools
    ) {
      tools(dependencies = object : ToolsComponent.Dependencies,
        Dependencies by props.dependencies {
        override val toolsOutput: (Output) -> Unit = ::toolsOutput
        override val toolsInput: Observable<ToolsController.Input> = this@ResearchScreen.toolsInputObservable
        override val open: Boolean = state.toolsOpen
        override val data: ResearchSlicesSizesDataNew = model.data!!
      })
    }

    mainContent(
      marginnLeft = if (state.toolsOpen) drawerWidth.px else 8.spacingUnits,
      marginnRight = if (state.marksOpen) drawerWidth.px else 16.spacingUnits
    ) {
      cuts(dependencies = object : CutsContainerViewComponent.Dependencies,
        Dependencies by props.dependencies {
        override val data: ResearchSlicesSizesDataNew = model.data!!
        override val cutsContainerInputs: Observable<CutsContainerController.Input> = this@ResearchScreen.cutsContainerInputObservable
        override val cutsContainerOutput: (CutsContainerController.Output) -> Unit = ::cutsContainerOutput
        override val cutsInput: Observable<CutController.Input> = this@ResearchScreen.cutsInputObservable
      })
    }

    rightDrawer(
      open = state.marksOpen,
      drawerWidth = if (state.marksOpen) drawerWidth.px else 16.spacingUnits,
      onOpen = ::openMarks,
      onClose = ::closeMarks
    ) {
      val sizesData = model.data
      covidMarks(dependencies = object : CovidMarksComponent.Dependencies,
        Dependencies by props.dependencies {
        override val marksOutput: (CovidMarksController.Output) -> Unit = ::covidMarksOutput
        override val marksInput: Observable<CovidMarksController.Input> =
          this@ResearchScreen.covidMarksInputObservable
        override val data: ResearchSlicesSizesDataNew = sizesData!!
        override val open: Boolean = state.marksOpen
      })
    }
  }

  private fun toolsOutput(output: Output) {
    when (output) {
      is Output.BlackChanged -> cutsInputObservable.onNext(
        CutController.Input.BlackChanged(output.value)
      )
      is Output.WhiteChanged -> cutsInputObservable.onNext(
        CutController.Input.WhiteChanged(output.value)
      )
      is Output.GammaChanged -> cutsInputObservable.onNext(
        CutController.Input.GammaChanged(output.value)
      )
      is Output.MipMethodChanged ->
        cutsInputObservable.onNext(CutController.Input.MipMethodChanged(output.mip))
      is Output.MipValueChanged ->
        cutsInputObservable.onNext(CutController.Input.MipValueChanged(output.value))
      is Output.PresetChanged ->
        cutsInputObservable.onNext(CutController.Input.PresetChanged(output.preset))
      is Output.GridChanged -> {
        cutsContainerInputObservable.onNext(CutsContainerController.Input.GridChanged(output.grid))
      }
      is Output.Back -> researchViewDelegate.dispatch(ResearchView.Event.BackToList)
      is Output.Close -> marksInputObservable.onNext(MarksController.Input.CloseResearchRequested)
    }
  }

  private fun cutsContainerOutput(output: CutsContainerController.Output) {
    when (output) {
      is CutsContainerController.Output.OpenFullCut -> toolsInputObservable.onNext(
        ToolsController.Input.OpenFullCut(output.cut)
      )
      is CutsContainerController.Output.CloseFullCut -> toolsInputObservable.onNext(
        ToolsController.Input.ReturnPreviousGrid(output.cut)
      )
      is CutsContainerController.Output.CircleDrawn -> marksInputObservable.onNext(
        MarksController.Input.AddNewMark(output.circle, output.sliceNumber, output.cut)
      )
      is CutsContainerController.Output.RectangleDrawn -> marksInputObservable.onNext(
        MarksController.Input.AddNewMark(output.rectangle, output.sliceNumber, output.cut)
      )
      is CutsContainerController.Output.SelectMark -> marksInputObservable.onNext(
        MarksController.Input.SelectMark(output.mark)
      )
      is CutsContainerController.Output.UnselectMark -> marksInputObservable.onNext(
        MarksController.Input.UnselectMark(output.mark)
      )
      is CutsContainerController.Output.ContrastBrightnessChanged -> toolsInputObservable.onNext(
        ToolsController.Input.ContrastBrightnessChanged(output.black, output.white)
      )
      is CutsContainerController.Output.UpdateMarkWithoutSave -> marksInputObservable.onNext(
        MarksController.Input.UpdateMarkWithoutSave(output.markToUpdate)
      )
      is CutsContainerController.Output.UpdateMarkWithSave -> marksInputObservable.onNext(
        MarksController.Input.UpdateMarkWithSave(output.mark)
      )
      is CutsContainerController.Output.ChangeCutType -> cutsContainerInputObservable.onNext(
        CutsContainerController.Input.ChangeCutType(output.cutType, output.cut)
      )
    }.let { }
  }

  private fun marksOutput(output: MarksController.Output) {
    when (output) {
      is MarksController.Output.Marks ->
        cutsInputObservable.onNext(CutController.Input.Marks(output.list))
      MarksController.Output.CloseResearch ->
        researchViewDelegate.dispatch(ResearchView.Event.Close)
      is MarksController.Output.CenterSelectedMark -> {
        cutsInputObservable.onNext(CutController.Input.ChangeSliceNumberByMarkCenter(output.mark))
        setState { dialogOpen = true }

      }
    }.let { }
  }

  private fun covidMarksOutput(output: CovidMarksController.Output) {
    when (output) {

      else -> TODO()
    }.let { }
  }

  private fun RBuilder.formDialog(open: Boolean) {
    fun handleClose() {
      setState { dialogOpen = false}
    }
    mDialog(open, onClose =  { _, _ -> handleClose() }) {
      mDialogTitle("Комментарий (можно оставить поле пустым) (5/5)")
      mDialogContent {
        mTextField("Комментарий", autoFocus = true, margin = MFormControlMargin.dense, type = InputType.text, fullWidth = true)
      }
      mDialogActions {
        mButton("Закрыть", onClick = { handleClose() }, variant = MButtonVariant.text)
        mButton("Подтвердить",  onClick = { handleClose() }, variant = MButtonVariant.text)
      }
    }
  }


  private fun updateState(model: ResearchView.Model) = setState { researchModel = model }
  private fun closeTools() = setState { toolsOpen = false }
  private fun openTools() = setState { toolsOpen = true }
  private fun closeMarks() = setState { marksOpen = false }
  private fun openMarks() = setState { marksOpen = true }

  override fun componentWillUnmount() {
    GlobalScope.launch {
      props.dependencies.marksRepository.clean()
      props.dependencies.brightnessRepository.clean()
      props.dependencies.mipRepository.clean()
    }
    lifecycleRegistry.destroy()
  }

  object ResearchStyles : StyleSheet("ResearchStyles", isStatic = true) {

    val appFrameContainerStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
    }

    val mainContentContainerStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
      height = 100.pct
      minHeight = 100.vh
    }

  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: ResearchRepository
    val marksRepository: MarksRepository
    val brightnessRepository: BrightnessRepository
    val mipRepository: MipRepository
    val researchOutput: (ResearchController.Output) -> Unit
    val research: Research
  }

}

class ResearchState(
  var toolsOpen: Boolean,
  var marksOpen: Boolean,
  var dialogOpen: Boolean,
  var researchModel: ResearchView.Model
) : RState

interface ResearchProps : RProps {
  var dependencies: ResearchScreen.Dependencies
}

fun RBuilder.research(dependencies: ResearchScreen.Dependencies) = child(ResearchScreen::class) {
  attrs.dependencies = dependencies
}
