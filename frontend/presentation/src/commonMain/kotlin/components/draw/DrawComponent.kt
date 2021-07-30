package components.draw

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.draw.Draw.Dependencies
import components.draw.Draw.Model
import components.getStore
import model.MouseDown
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

  override fun onMouseDown(mouseDownModel: MouseDown) {
    store.accept(mouseDownModel.toIntent())
  }

  override fun onMouseMove(dicomX: Double, dicomY: Double) {
    store.accept(MyDrawStore.Intent.Move(dicomX = dicomX, dicomY = dicomY))
  }

  override fun onMouseUp() {
    store.accept(MyDrawStore.Intent.MouseUp)
  }

  override fun onMouseOut() {
    store.accept(MyDrawStore.Intent.MouseOut)
  }

  override fun onMouseWheel(dicomDeltaY: Int) {
    store.accept(MyDrawStore.Intent.MouseWheel(deltaDicomY = dicomDeltaY))
  }

  override fun onDoubleClick() {
    store.accept(MyDrawStore.Intent.DoubleClick)
  }

  override fun onScreenDimensionChanged(clientHeight: Int?, clientWidth: Int?) {
    if (clientHeight != null && clientWidth != null) {
      println("MY: onScreenDimensionChanged clientHeight != null && clientWidth != null")
      val clientHeightDiff = clientHeight != model.value.screenDimensionsModel.originalScreenHeight
      val clientWidthDiff = clientWidth != model.value.screenDimensionsModel.originalScreenWidth
      val updateDimensions = clientHeightDiff || clientWidthDiff
      println("MY: model.value.screenDimensionsModel = ${model.value.screenDimensionsModel}")
      println("MY: clientHeight = $clientHeight")
      println("MY: clientWidth = $clientWidth")
      if (updateDimensions) {
        val calculateScreenDimensions = plane.calculateScreenDimensions(clientHeight, clientWidth)
        store.accept(MyDrawStore.Intent.UpdateScreenDimensions(calculateScreenDimensions))
      }
    }
  }
}