package components.shapes

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Plane
import model.PointPosition
import model.Shape
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
        plane = plane
      ),
      bootstrapper = SimpleBootstrapper(),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    data class PointPositionChanged(val position: PointPosition?) : Result()
    data class HounsfieldChanged(val hu: Double) : Result()
    data class Shapes(val shapes: List<Shape>) : Result()
  }

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
      }
    }


  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.PointPositionChanged -> copy(position = result.position)
        is Result.Shapes -> copy(shapes = result.shapes)
        is Result.HounsfieldChanged -> copy(hounsfield = result.hu.toInt())
      }
  }
}