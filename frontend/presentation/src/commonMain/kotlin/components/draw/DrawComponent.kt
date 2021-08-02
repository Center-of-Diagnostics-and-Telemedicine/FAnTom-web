package components.draw

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.observable.subscribe
import components.asValue
import components.draw.Draw.Dependencies
import components.draw.Draw.Model
import components.getStore
import model.MouseDown
import model.ScreenDimensionsModel
import store.draw.MyDrawStore

class DrawComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Draw, ComponentContext by componentContext, Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      DrawStoreProvider(
        storeFactory = storeFactory,
        researchId = researchId,
        plane = plane
      ).provide()
    }

  override val model: Value<Model> = store.asValue().map(stateToModel)

  init {
    store.labels.mapNotNull(labelsToOutput).subscribe(onNext = drawOutput::onNext)
  }

  override fun onMouseDown(mouseDownModel: MouseDown) {
    store.accept(mouseDownModel.toIntent(dimensions = model.value.screenDimensionsModel))
  }

  override fun onMouseMove(screenX: Double, screenY: Double) {
    val dimensions = model.value.screenDimensionsModel
    val dicomX = mapScreenXToDicomX(
      screenX = screenX,
      horizontalRatio = dimensions.horizontalRatio
    )
    val dicomY = mapScreenYToDicomY(
      screenY = screenY,
      verticalRatio = dimensions.verticalRatio
    )
    store.accept(MyDrawStore.Intent.Move(dicomX = dicomX, dicomY = dicomY))
  }

  override fun onMouseUp() {
    store.accept(MyDrawStore.Intent.MouseUp)
  }

  override fun onMouseOut() {
    store.accept(MyDrawStore.Intent.MouseOut)
  }

  override fun onMouseWheel(screenDeltaY: Double) {
    val deltaDicomY = if (screenDeltaY < 0.0) -1 else 1
    store.accept(MyDrawStore.Intent.MouseWheel(deltaDicomY = deltaDicomY))
  }

  override fun onDoubleClick() {
    store.accept(MyDrawStore.Intent.DoubleClick)
  }

  override fun onScreenDimensionChanged(clientHeight: Int?, clientWidth: Int?) {
    shouldUpdateDimensions(clientHeight, clientWidth) { dimensions ->
      store.accept(MyDrawStore.Intent.UpdateScreenDimensions(dimensions))
    }
  }

  private inline fun shouldUpdateDimensions(
    clientHeight: Int?,
    clientWidth: Int?,
    block: (should: ScreenDimensionsModel) -> Unit
  ) {
    if (clientHeight != null && clientWidth != null) {
      val clientHeightDiff = clientHeight != model.value.screenDimensionsModel.originalScreenHeight
      val clientWidthDiff = clientWidth != model.value.screenDimensionsModel.originalScreenWidth
      val updateDimensions = clientHeightDiff || clientWidthDiff
      if (updateDimensions) {
        val newDimensions = plane.calculateScreenDimensions(clientHeight, clientWidth)
        block(newDimensions)
      }
    }
  }
}