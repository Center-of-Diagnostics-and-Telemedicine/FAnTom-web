package presentation.di

import client.datasource.remote.*
import client.domain.executor.Executor
import client.domain.repository.*
import client.newmvi.ResearchContainer
import client.newmvi.cut.binder.CutBinder
import client.newmvi.cut.store.*
import client.newmvi.draw.binder.DrawBinder
import client.newmvi.draw.store.DrawStore
import client.newmvi.draw.store.DrawStoreImpl
import client.newmvi.login.binder.LoginBinder
import client.newmvi.login.store.*
import client.newmvi.menu.black.binder.BlackBinder
import client.newmvi.menu.black.store.BlackStore
import client.newmvi.menu.black.store.BlackStoreImpl
import client.newmvi.menu.gamma.binder.GammaBinder
import client.newmvi.menu.gamma.store.GammaStore
import client.newmvi.menu.gamma.store.GammaStoreImpl
import client.newmvi.menu.mipmethod.binder.MipMethodBinder
import client.newmvi.menu.mipmethod.store.MipMethodStore
import client.newmvi.menu.mipmethod.store.MipMethodStoreImpl
import client.newmvi.menu.mipvalue.binder.MipValueBinder
import client.newmvi.menu.mipvalue.store.MipValueStore
import client.newmvi.menu.mipvalue.store.MipValueStoreImpl
import client.newmvi.menu.preset.binder.PresetBinder
import client.newmvi.menu.preset.store.PresetStore
import client.newmvi.menu.preset.store.PresetStoreImpl
import client.newmvi.menu.white.binder.WhiteBinder
import client.newmvi.menu.white.store.WhiteStore
import client.newmvi.menu.white.store.WhiteStoreImpl
import client.newmvi.researchlist.binder.ResearchListBinder
import client.newmvi.researchlist.store.*
import client.newmvi.researchmvi.binder.ResearchBinder
import client.newmvi.researchmvi.store.*
import client.newmvi.shapes.binder.ShapesBinder
import client.newmvi.shapes.store.*
import client.newmvi.slider.binder.SliderBinder
import client.newmvi.slider.store.SliderStore
import client.newmvi.slider.store.SliderStoreImpl
import com.badoo.reaktive.observable.Observable
import data.JsLocalDataSource
import model.*
import presentation.executor.JsExecutor

val executor: Executor = JsExecutor()

val local = JsLocalDataSource()

val remoteLoginDataSource: LoginRemote = LoginRemoteDataSource()
val loginRepository: LoginRepository = LoginRepositoryImpl(
  local = local,
  remote = remoteLoginDataSource
)

val remoteResearchDataSource: ResearchRemote = ResearchRemoteDataSource()
val researchRepository: ResearchRepository = ResearchRepositoryImpl(
  local = local,
  remote = remoteResearchDataSource
)

val loginProcessor: LoginProcessor = LoginProcessorImpl(loginRepository)
val loginStore: LoginStore = LoginStoreImpl(loginProcessor)
val loginBinder = LoginBinder(loginStore)
fun injectLogin(): LoginBinder = loginBinder

val researchListLoader: ResearchListLoader = ResearchListLoaderImpl(researchRepository)
val researchListStore: ResearchListStore = ResearchListStoreImpl(researchListLoader)
val researchListBinder = ResearchListBinder(researchListStore)
fun injectResearchList(): ResearchListBinder = researchListBinder

val hounsfieldDataLoader: HounsfieldDataLoader = HounsfieldDataLoaderImpl(researchRepository)
val researchLoader: ResearchDataLoader = ResearchDataLoaderImpl(researchRepository)
val gridProcessor: GridProcessor = GridProcessorImpl()
val confirmMarkProcessor: ConfirmCtTypeForResearchResearchProcessor = ConfirmCtTypeForResearchResearchProcessorImpl(
  researchRepository
)
val closeSessionProcessor: CloseSessionProcessor = CloseSessionProcessorImpl(researchRepository)
val researchStore: ResearchStore = ResearchStoreImpl(
  researchDataLoader = researchLoader,
  slicesSizesDataListener = ResearchContainer.sliceSizesDataObservable,
  areaDeletedListener = ResearchContainer.areaDeletedObservable,
  areaSavedListener = ResearchContainer.newAreaObservable,
  deleteClickObservable = ResearchContainer.deleteClickObservable,
  gridProcessor = gridProcessor,
  callToCloseResearchListener = ResearchContainer.callToCloseResearchListener,
  callBackToResearchListListener = ResearchContainer.callBackToResearchListListener,
  confirmMarkProcessor = confirmMarkProcessor,
  closeSessionProcessor = closeSessionProcessor
)

fun injectNewResearch(): ResearchBinder = ResearchBinder(
  store = researchStore,
  changeCutTypeModelObservable = ResearchContainer.changeCutTypeListener,
  closeResearchObservable = ResearchContainer.closeResearchObservable
)

val cutLoader: CutLoader = CutLoaderImpl(researchRepository)

val axialCutStore: CutStore = CutStoreImpl(
  cutLoader,
  SLYCE_TYPE_AXIAL
)
val frontalCutStore: CutStore = CutStoreImpl(
  cutLoader,
  SLYCE_TYPE_FRONTAL
)
val sagittalCutStore: CutStore = CutStoreImpl(
  cutLoader,
  SLYCE_TYPE_SAGITTAL
)
val axialCutBinder = CutBinder(
  store = axialCutStore,
  sliceNumberObservable = ResearchContainer.axialSliceNumberObservable,
  cutType = SLYCE_TYPE_AXIAL,
  sliceSizeDataObservable = getSliceSizesDataObservable(SLYCE_TYPE_AXIAL),
  blackAndWhiteObservable = ResearchContainer.axialBlackAndWhiteObservable
)
val frontalCutBinder = CutBinder(
  store = frontalCutStore,
  sliceNumberObservable = ResearchContainer.frontalSliceNumberObservable,
  cutType = SLYCE_TYPE_FRONTAL,
  sliceSizeDataObservable = getSliceSizesDataObservable(SLYCE_TYPE_FRONTAL),
  blackAndWhiteObservable = ResearchContainer.frontalBlackAndWhiteObservable
)
val sagittalCutBinder = CutBinder(
  store = sagittalCutStore,
  sliceNumberObservable = ResearchContainer.sagittalSliceNumberObservable,
  cutType = SLYCE_TYPE_SAGITTAL,
  sliceSizeDataObservable = getSliceSizesDataObservable(SLYCE_TYPE_SAGITTAL),
  blackAndWhiteObservable = ResearchContainer.sagittalBlackAndWhiteObservable
)

fun injectCut(cutType: Int): CutBinder {
  return when (cutType) {
    SLYCE_TYPE_AXIAL -> axialCutBinder
    SLYCE_TYPE_FRONTAL -> frontalCutBinder
    SLYCE_TYPE_SAGITTAL -> sagittalCutBinder
    else -> throw NotImplementedError("maybe you forgot to add something? injectCut")
  }
}

fun getSliceSizesDataObservable(cutType: Int): Observable<SliceSizeData> =
  when (cutType) {
    SLYCE_TYPE_AXIAL ->
      ResearchContainer.axialSlicesSizesDataObservable
    SLYCE_TYPE_SAGITTAL ->
      ResearchContainer.sagittalSlicesSizesDataObservable
    SLYCE_TYPE_FRONTAL ->
      ResearchContainer.frontalSlicesSizesDataObservable
    else -> throw NotImplementedError("maybe you forgot to add something? getSliceNumberObservable")
  }

val axialSliderStore: SliderStore = SliderStoreImpl(ResearchContainer.axialSliceNumberObservable)
val frontalSliderStore: SliderStore = SliderStoreImpl(ResearchContainer.frontalSliceNumberObservable)
val sagittalSliderStore: SliderStore = SliderStoreImpl(ResearchContainer.sagittalSliceNumberObservable)

val axialSliderBinder = SliderBinder(axialSliderStore, ResearchContainer.axialSliceNumberObservable)
val frontalSliderBinder = SliderBinder(
  frontalSliderStore,
  ResearchContainer.frontalSliceNumberObservable
)
val sagittalSliderBinder = SliderBinder(
  sagittalSliderStore,
  ResearchContainer.sagittalSliceNumberObservable
)

fun injectSlider(cutType: Int): SliderBinder {
  return when (cutType) {
    SLYCE_TYPE_AXIAL -> axialSliderBinder
    SLYCE_TYPE_SAGITTAL -> sagittalSliderBinder
    SLYCE_TYPE_FRONTAL -> frontalSliderBinder
    else -> throw NotImplementedError("maybe you forgot to add something? injectCut")
  }
}

val whiteStore: WhiteStore = WhiteStoreImpl(
  ResearchContainer.whiteValueListener
)

fun injectWhite(): WhiteBinder = WhiteBinder(
  whiteStore,
  ResearchContainer.whiteValueObservable
)

val blackStore: BlackStore = BlackStoreImpl(
  ResearchContainer.blackValueListener
)

fun injectBlack(): BlackBinder = BlackBinder(
  blackStore,
  ResearchContainer.blackValueObservable
)

val gammaStore: GammaStore = GammaStoreImpl(
  ResearchContainer.gammaValueObservable
)

fun injectGamma(): GammaBinder = GammaBinder(
  gammaStore,
  ResearchContainer.gammaValueObservable
)

val mipMethodStore: MipMethodStore = MipMethodStoreImpl(
  ResearchContainer.mipMethodObservable
)

fun injectMipMethod(): MipMethodBinder = MipMethodBinder(
  mipMethodStore,
  ResearchContainer.mipMethodObservable
)

val mipValueStore: MipValueStore = MipValueStoreImpl(
  ResearchContainer.mipValueObservable
)

fun injectMipValue(): MipValueBinder = MipValueBinder(
  mipValueStore,
  ResearchContainer.mipValueObservable,
  ResearchContainer.showMipValueObservable
)

val presetStore: PresetStore = PresetStoreImpl(
  ResearchContainer.presetObservable
)

fun injectPreset(): PresetBinder = PresetBinder(
  presetStore,
  ResearchContainer.presetObservable
)

val axialDrawStore: DrawStore = DrawStoreImpl(
  areaObservable = ResearchContainer.axialNewCircleObservable,
  mouseDataObservable = ResearchContainer.axialMouseDataObservable,
  contrastBrightnessListener = ResearchContainer.axialContrastBrightnessListener,
  contrastBrightnessEndListener = ResearchContainer.axialContrastBrightnessEndListener,
  mouseMoveListener = ResearchContainer.axialMouseMoveObservable,
  mouseClickObservable = ResearchContainer.axialMouseClickObservable,
  mouseDownListener = ResearchContainer.axialMouseDownObservable,
  mouseUpListener = ResearchContainer.mouseUpListener,
  sliceNumberMoveListener = ResearchContainer.axialSliceNumberMoveObservable
)
val frontalDrawStore: DrawStore = DrawStoreImpl(
  areaObservable = ResearchContainer.frontalNewCircleObservable,
  mouseDataObservable = ResearchContainer.frontalMouseDataObservable,
  contrastBrightnessListener = ResearchContainer.frontalContrastBrightnessListener,
  contrastBrightnessEndListener = ResearchContainer.frontalContrastBrightnessEndListener,
  mouseMoveListener = ResearchContainer.frontalMouseMoveObservable,
  mouseClickObservable = ResearchContainer.frontalMouseClickObservable,
  mouseDownListener = ResearchContainer.frontalMouseDownObservable,
  mouseUpListener = ResearchContainer.mouseUpListener,
  sliceNumberMoveListener = ResearchContainer.frontalSliceNumberMoveObservable
)
val sagittalDrawStore: DrawStore = DrawStoreImpl(
  areaObservable = ResearchContainer.sagittalNewCircleObservable,
  mouseDataObservable = ResearchContainer.sagittalMouseDataObservable,
  contrastBrightnessListener = ResearchContainer.sagittalContrastBrightnessListener,
  contrastBrightnessEndListener = ResearchContainer.sagittalContrastBrightnessEndListener,
  mouseMoveListener = ResearchContainer.sagittalMouseMoveObservable,
  mouseClickObservable = ResearchContainer.sagittalMouseClickObservable,
  mouseDownListener = ResearchContainer.sagittalMouseDownObservable,
  mouseUpListener = ResearchContainer.mouseUpListener,
  sliceNumberMoveListener = ResearchContainer.sagittalSliceNumberMoveObservable
)
val axialDrawBinder = DrawBinder(axialDrawStore)
val frontalDrawBinder = DrawBinder(frontalDrawStore)
val sagittalDrawBinder = DrawBinder(sagittalDrawStore)

fun injectDrawCanvas(cutType: Int): DrawBinder =
  when (cutType) {
    SLYCE_TYPE_AXIAL -> axialDrawBinder
    SLYCE_TYPE_SAGITTAL -> sagittalDrawBinder
    SLYCE_TYPE_FRONTAL -> frontalDrawBinder
    else -> throw NotImplementedError("maybe you forgot to add something? getSliceNumberObservable")
  }

val axialShapesStore: ShapesStore = ShapesStoreImpl(
  SLYCE_TYPE_AXIAL,
  hounsfieldDataLoader,
  ResearchContainer.changeCutTypeListener
)
val frontalShapesStore: ShapesStore = ShapesStoreImpl(
  SLYCE_TYPE_FRONTAL,
  hounsfieldDataLoader,
  ResearchContainer.changeCutTypeListener
)
val sagittalShapesStore: ShapesStore = ShapesStoreImpl(
  SLYCE_TYPE_SAGITTAL,
  hounsfieldDataLoader,
  ResearchContainer.changeCutTypeListener
)
val axialShapesBinder = ShapesBinder(
  axialShapesStore,
  ResearchContainer.axialCirclesObservable,
  ResearchContainer.axialLinesObservable,
  ResearchContainer.axialPositionDataObservable,
  ResearchContainer.axialSliceNumberObservable,
  ResearchContainer.axialMoveRectsObservable
)
val frontalShapesBinder = ShapesBinder(
  sagittalShapesStore,
  ResearchContainer.frontalCirclesObservable,
  ResearchContainer.frontalLinesObservable,
  ResearchContainer.frontalPositionDataObservable,
  ResearchContainer.frontalSliceNumberObservable,
  ResearchContainer.frontalMoveRectsObservable
)
val sagittalShapesBinder = ShapesBinder(
  frontalShapesStore,
  ResearchContainer.sagittalCirclesObservable,
  ResearchContainer.sagittalLinesObservable,
  ResearchContainer.sagittalPositionDataObservable,
  ResearchContainer.sagittalSliceNumberObservable,
  ResearchContainer.sagittalMoveRectsObservable
)

fun injectShapesCanvas(cutType: Int): ShapesBinder =
  when (cutType) {
    SLYCE_TYPE_AXIAL -> axialShapesBinder
    SLYCE_TYPE_FRONTAL -> frontalShapesBinder
    SLYCE_TYPE_SAGITTAL -> sagittalShapesBinder
    else -> throw NotImplementedError("maybe you forgot to add something? getSliceNumberObservable")

  }