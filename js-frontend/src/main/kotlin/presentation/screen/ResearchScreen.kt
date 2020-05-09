package presentation.screen

import client.ctTypes
import client.newmvi.researchmvi.view.ResearchView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.*
import com.ccfraser.muirwik.components.dialog.*
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.input.mInputLabel
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.menu.mMenuItem
import kotlinext.js.jsObject
import kotlinx.css.*
import kotlinx.css.Position
import kotlinx.html.DIV
import model.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import presentation.di.injectNewResearch
import presentation.screen.ComponentStyles.appFrameContainerStyle
import presentation.screen.ComponentStyles.columnOfRowsStyle
import presentation.screen.ComponentStyles.leftDrawerHeaderStyle
import presentation.screen.ComponentStyles.mainContentContainerStyle
import presentation.screen.research.menu.leftMenu
import presentation.screen.viewcomponents.*
import react.*
import styled.*
import kotlin.browser.window

class ResearchScreen(props: ResearchProps) :
  RComponent<ResearchProps, ResearchState>(props), ResearchView {

  private val drawerWidth = 300
  private val binder = injectNewResearch()
  override val events: PublishSubject<ResearchView.Event> = PublishSubject()

  init {
    state = ResearchState(
      loading = true,
      showError = false,
      error = "",
      data = null,
      leftDrawerOpen = false,
      rightDrawerOpen = false,
      gridModel = null,
      ctTypeToConfirm = null,
      currentGrid = CutsGridType.THREE,
      confirmationDialogOpen = false,
      leftPercent = "",
      rightPercent = "",
      nonRelevant = false
    )
  }

  override fun show(model: ResearchView.ResearchViewModel) {
    if (model.studyCompleted) {
      dispatch(ResearchView.Event.Clear)
      props.onClose()
    } else {
      setState {
        loading = model.isLoading
        error = model.error
        showError = model.error.isNotEmpty()
        data = model.data
        gridModel = model.gridModel
        ctTypeToConfirm = model.ctTypeToConfirm
        if (model.ctTypeToConfirm != null) {
          confirmationDialogOpen = true
        }
      }
    }
  }

  private val cellDoubleClickListener: (CellModel) -> Unit = { cellModel ->
    dispatch(ResearchView.Event.CellFullMode(cellModel))
  }

  override fun componentDidMount() {
    binder.attachView(this@ResearchScreen)
    binder.onStart()
    dispatch(ResearchView.Event.Init(props.researchId))
    window.addEventListener(type = "keydown", callback = {
      val keyboardEvent = it as KeyboardEvent
      if (keyboardEvent.keyCode == 46)
        dispatch(ResearchView.Event.Delete)
    })
  }

  override fun RBuilder.render() {
    mCssBaseline()
    alert(
      message = state.error,
      open = state.showError,
      handleClose = { dispatch(ResearchView.Event.ErrorShown) }
    )
    themeContext.Consumer { _ ->
      //container for drawers and main content
      styledDiv {
        css(appFrameContainerStyle)

        //leftDrawer
        leftDrawer()

        val gridModel = state.gridModel
        //mainContent
        styledDiv {
          css(mainContentContainerStyle)
          css {
            marginLeft = if (state.leftDrawerOpen) drawerWidth.px else 7.spacingUnits
//            marginRight = if (state.rightDrawerOpen) 350.px else 7.spacingUnits
          }
          loading(state.loading)

          val data = state.data
          if (data != null && gridModel != null) {
            styledDiv {
              css(columnOfRowsStyle)
              when (gridModel.type) {
                CutsGridType.SINGLE -> singleCutContainer(
                  gridModel.cells[0],
                  cellDoubleClickListener
                )
                CutsGridType.TWO_VERTICAL -> twoVerticalCutsContainer(
                  gridModel.cells,
                  cellDoubleClickListener
                )
                CutsGridType.TWO_HORIZONTAL -> twoHorizontalCutsContainer(
                  gridModel.cells,
                  cellDoubleClickListener
                )
                CutsGridType.THREE -> threeCutsContainer(
                  gridModel.cells,
                  cellDoubleClickListener
                )
                CutsGridType.FOUR -> TODO()//fourCutsContainer()
              }
            }
          }
        }
        rightDrawer()
        ctTypes.firstOrNull { it.ctType == state.ctTypeToConfirm }?.let {
          confirmationDialog(state.confirmationDialogOpen, it)
        }
        nonRelevantDialog()
      }
    }
  }

  private fun RBuilder.nonRelevantDialog() {
    fun closeAlertDialog() {
      setState { nonRelevant = false }
    }

    mDialog(
      state.nonRelevant,
      onClose = { _, _ -> closeAlertDialog() },
      transitionComponent = null
    ) {
      mDialogTitle("Подтвердите выбор")
      mDialogContent {
        mTypography(text = "Вы выбрали 'Нерелевантная серия'") {
          css {
            marginBottom = 3.spacingUnits
          }
        }
      }
      mDialogActions {
        mButton(
          variant = MButtonVariant.contained,
          caption = "Отмена",
          color = MColor.primary,
          onClick = { closeAlertDialog() })
        mButton(
          variant = MButtonVariant.contained,
          caption = "Подтвердить",
          color = MColor.primary,
          onClick = {
            dispatch(
              ResearchView.Event.ConfirmCtType(
                ctType = CTType.NON_RELEVANT,
                leftPercent = "0",
                rightPercent = "0"
              )
            )
            closeAlertDialog()
          })
      }
    }
  }

  private fun RBuilder.confirmationDialog(
    open: Boolean,
    ctTypeModel: CTTypeModel
  ) {
    fun closeAlertDialog() {
      setState { confirmationDialogOpen = false }
    }

    val noPercentEntered = state.leftPercent.isEmpty() || state.rightPercent.isEmpty()

    mDialog(open, onClose = { _, _ -> closeAlertDialog() }, transitionComponent = null) {
      mDialogTitle("Подтвердите выбор ${ctTypeModel.name}")
      mDialogContent {
        mTypography(text = "Вы выбрали ${ctTypeModel.name}, поражение легких: ")
        if (noPercentEntered) {
          mTypography(text = "Пожалуйста, введите проценты поражения легких")
        } else {
          mTypography(text = "Левое ${state.leftPercent}%")
          mTypography(text = "Правое ${state.rightPercent}%")
        }
      }
      mDialogActions {
        mButton(
          variant = MButtonVariant.contained,
          caption = "Отмена",
          color = MColor.primary,
          onClick = { closeAlertDialog() })
        if (!noPercentEntered) {
          mButton(
            variant = MButtonVariant.contained,
            caption = "Подтвердить",
            color = MColor.primary,
            onClick = {
              dispatch(
                ResearchView.Event.ConfirmCtType(
                  ctTypeModel.ctType,
                  state.leftPercent,
                  state.rightPercent
                )
              )
              closeAlertDialog()
            })
        }
      }
    }
  }

  private fun StyledDOMBuilder<DIV>.leftDrawer() {
    val pp: MPaperProps = jsObject { }
    pp.asDynamic().style = kotlinext.js.js {
      height = "100%"
      minHeight = "100vh"
      if (!state.leftDrawerOpen) {
        overflowX = "hidden"
        width = 7.spacingUnits.value
      } else {
        width = drawerWidth + 1
      }
    }
    mDrawer(
      state.leftDrawerOpen,
      MDrawerAnchor.left,
      MDrawerVariant.permanent,
      paperProps = pp
    ) {

      attrs.onMouseEnter = { setState { leftDrawerOpen = true } }
      attrs.onMouseLeave = { setState { leftDrawerOpen = false } }
      styledDiv {
        css(leftDrawerHeaderStyle)
        if (state.leftDrawerOpen) {
          cutsGrid(cutsGridClickListener = {
            if (state.currentGrid != it) {
              dispatch(ResearchView.Event.GridChanged(it))
            }
          })
          mIconButton(
            "chevron_left",
            onClick = { setState { leftDrawerOpen = false } }
          )
        } else {
          mIconButton(
            "chevron_right",
            onClick = { setState { leftDrawerOpen = true } }
          )
        }
      }
      mDivider()
      styledDiv {
        css {
          justifyContent = JustifyContent.spaceBetween
        }
        leftMenu(state.leftDrawerOpen)
        mListItemWithIcon(primaryText = "Все исследования", iconName = "keyboard_backspace") {
          attrs {
            onClick = { dispatch(ResearchView.Event.Back) }
          }
        }
      }
    }
  }

  private fun StyledDOMBuilder<DIV>.rightDrawer() {
    styledDiv {
      css {
        display = Display.flex
        flexDirection = FlexDirection.column
        width = 16.spacingUnits
        padding(1.spacingUnits)
      }
      ctTypes.forEach {
        buttonWithPopover(
          text = it.name,
          color = it.color,
          popoverText = it.description,
          ctType = it.ctType
        )
      }
      mInputLabel("Левое", htmlFor = "left-input") {
        css {
          marginTop = 1.spacingUnits
        }
      }
      val inputProps: RProps = jsObject { }
      inputProps.asDynamic().name = "left"
      inputProps.asDynamic().id = "left-input"
      mTooltip(title = "Процент поражения левого легкого", placement = TooltipPlacement.left) {
        mSelect(
          state.leftPercent ?: "",
          name = "left",
          variant = MFormControlVariant.outlined,
          onChange = { event, _ -> handleLeft(event) }) {
          attrs.inputProps = inputProps
          mMenuItem("Выберите", value = "")
          mMenuItem("0%", value = "0")
          mMenuItem("10%", value = "10")
          mMenuItem("20%", value = "20")
          mMenuItem("30%", value = "30")
          mMenuItem("40%", value = "40")
          mMenuItem("50%", value = "50")
          mMenuItem("60%", value = "60")
          mMenuItem("70%", value = "70")
          mMenuItem("80%", value = "80")
          mMenuItem("90%", value = "90")
          mMenuItem("100%", value = "100")
        }
      }


      mInputLabel("Правое", htmlFor = "right-input") {
        css {
          marginTop = 1.spacingUnits
        }
      }
      val inputProps2: RProps = jsObject { }
      inputProps2.asDynamic().name = "right"
      inputProps2.asDynamic().id = "right-input"
      mTooltip(title = "Процент поражения правого легкого", placement = TooltipPlacement.left) {
        mSelect(
          state.rightPercent ?: "",
          name = "right",
          variant = MFormControlVariant.outlined,
          onChange = { event, _ -> handleRight(event) }) {
          attrs.inputProps = inputProps2
          mMenuItem("Выберите", value = "")
          mMenuItem("0%", value = "0")
          mMenuItem("10%", value = "10")
          mMenuItem("20%", value = "20")
          mMenuItem("30%", value = "30")
          mMenuItem("40%", value = "40")
          mMenuItem("50%", value = "50")
          mMenuItem("60%", value = "60")
          mMenuItem("70%", value = "70")
          mMenuItem("80%", value = "80")
          mMenuItem("90%", value = "90")
          mMenuItem("100%", value = "100")
        }
        css {
          marginBottom = 1.spacingUnits
        }
      }
      mButton(
        "Нерелеван. серия",
        variant = MButtonVariant.contained,
        color = MColor.primary,
        onClick = { setState { nonRelevant = true } }) {
        css {
          height = 100.pct
        }
      }
    }
  }

  private fun handleRight(event: Event) {
    val value = event.targetValue
    setState { rightPercent = value.toString() }
  }

  private fun handleLeft(event: Event) {
    val value = event.targetValue
    setState { leftPercent = value.toString() }
  }

  private fun StyledDOMBuilder<DIV>.buttonWithPopover(
    text: String,
    color: String,
    popoverText: String,
    ctType: CTType
  ) {
    val disabled = state.leftPercent?.isNullOrEmpty() || state.rightPercent?.isNullOrEmpty()
    styledDiv {
      css {
        height = 100.pct
        padding(1.spacingUnits)
      }
      mTooltip(title = popoverText, placement = TooltipPlacement.left) {
        mButton(
          text,
          disabled = disabled,
          onClick = { dispatch(ResearchView.Event.CTTypeChosen(ctType)) }
        ) {
          attrs {
            fullWidth = true
          }
          css {
            height = 100.pct
            background = "linear-gradient(45deg, $color 100%, $color 100%)"
          }
        }
      }
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: ResearchView.Event) {
    events.onNext(event)
  }
}

object ComponentStyles : StyleSheet("ComponentStyles", isStatic = true) {
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
  val cutContainerStyle by css {
    flex(1.0)
    display = Display.flex
    flexDirection = FlexDirection.column
    position = Position.relative
  }
  val blackContainerStyle by css {
    flex(1.0)
    display = Display.flex
    flexDirection = FlexDirection.column
    background = "#000"
    textAlign = TextAlign.center
    position = Position.relative
  }
  val rightDrawerHeaderStyle by css {
    display = Display.flex
    alignItems = Align.center
    justifyContent = JustifyContent.flexStart
  }
  val leftDrawerHeaderStyle by css {
    display = Display.flex
    alignItems = Align.center
    justifyContent = JustifyContent.flexEnd
  }
}

class ResearchState(
  var loading: Boolean = true,
  var showError: Boolean = false,
  var error: String = "",
  var data: ResearchSlicesSizesData? = null,
  var leftDrawerOpen: Boolean = false,
  var rightDrawerOpen: Boolean = false,
  var gridModel: GridModel? = null,
  var ctTypeToConfirm: CTType? = null,
  var currentGrid: CutsGridType = CutsGridType.THREE,
  var confirmationDialogOpen: Boolean = false,
  var leftPercent: String = "",
  var rightPercent: String = "",
  var nonRelevant: Boolean = false
) : RState


interface ResearchProps : RProps {
  var onClose: () -> Unit
  var researchId: Int
}

fun RBuilder.research(
  researchId: Int,
  onClose: () -> Unit
) = child(ResearchScreen::class) {
  attrs.researchId = researchId
  attrs.onClose = onClose
}