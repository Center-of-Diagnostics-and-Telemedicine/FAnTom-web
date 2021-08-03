package components.shapes

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import store.shapes.MyShapesStore
import store.shapes.MyShapesStore.*

internal class ShapesStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val plane: Plane
) {

  fun provide(): MyShapesStore =
    object : MyShapesStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MyShapesStore_${researchId}_${plane.type.intType}",
      initialState = State(
        sliceNumber = 1,
        position = null,
        shapes = listOf(),
        hounsfield = null,
        cutType = plane.type,
        plane = plane,
        screenDimensionsModel = initialScreenDimensionsModel()
      ),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleMousePosition -> TODO()
        is Intent.HandleSliceNumberChange -> TODO()
        is Intent.HandleShapes -> dispatch(Result.Shapes(intent.shapes))
        is Intent.UpdateScreenDimensions -> dispatch(Result.ScreenDimensionsChanged(intent.dimensions))
      }.let { }
    }

  }

  private sealed class Result : JvmSerializable {
    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    data class PointPositionChanged(val position: PointPosition?) : Result()
    data class HounsfieldChanged(val hu: Double) : Result()
    data class Shapes(val shapes: List<Shape>) : Result()
    data class ScreenDimensionsChanged(val dimensions: ScreenDimensionsModel) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.PointPositionChanged -> copy(position = result.position)
        is Result.Shapes -> copy(shapes = result.shapes)
        is Result.HounsfieldChanged -> copy(hounsfield = result.hu.toInt())
        is Result.ScreenDimensionsChanged -> copy(screenDimensionsModel = result.dimensions)
      }
  }
}