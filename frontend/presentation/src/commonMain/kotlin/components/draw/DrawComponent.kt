package components.draw

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.badoo.reaktive.observable.mapNotNull
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

  init {
    bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      drawInput.mapNotNull(inputToIntent) bindTo store
      store.labels.mapNotNull(labelsToOutput) bindTo drawOutput
    }
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
}