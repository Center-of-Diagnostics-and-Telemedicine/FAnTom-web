package store.cut

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Cut
import store.cut.ShapesStore.Intent
import store.cut.ShapesStore.State

abstract class ShapesStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Cut,
  private val researchId: Int
) {

  val initialState: State = State(
    horizontalCoefficient = 0.0,
    verticalCoefficient = 0.0,
    sliceNumber = 1
  )

  fun create(): ShapesStore =
    object : ShapesStore,
      Store<Intent, State, Nothing> by storeFactory.create(
        name = "ShapesStoreType${cut.type.intType}Id${researchId}",
        initialState = initialState,
        executorFactory = ::createExecutor,
        reducer = ReducerImpl
      ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing>

  protected sealed class Result : JvmSerializable {
    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    class HorizontalLineChanged(val coefficient: Double) : Result()
    class VerticalLineChanged(val coefficient: Double) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.HorizontalLineChanged -> copy(horizontalCoefficient = result.coefficient)
        is Result.VerticalLineChanged -> copy(verticalCoefficient = result.coefficient)
      }
  }
}
