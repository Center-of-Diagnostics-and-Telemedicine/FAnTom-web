package store.userinput

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.atomic.AtomicInt
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Plane
import model.Research
import store.userinput.UserInputStore.*


abstract class UserInputStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Plane,
  private val research: Research
) {

  val initialState: State = State(
    startDicomX = 0.0,
    startDicomY = 0.0,
    isContrastBrightness = false
  )

  fun create(): UserInputStore =
    object : UserInputStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "UserInputStoreType${cut.type.intType}Id${research.id}index${storeIndex.addAndGet(1)}",
        initialState = initialState,
        executorFactory = ::createExecutor,
        reducer = ReducerImpl
      ) {
      init {
        ensureNeverFrozen()
      }
    }

  private companion object {
    private val storeIndex = AtomicInt(0)
  }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Result()
    data class ContrastBrightness(val dicomX: Double, val dicomY: Double) : Result()
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Result()

    object Idle : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.StartContrastBrightness -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isContrastBrightness = true
        )
        is Result.ContrastBrightness -> copy(
          startDicomX = result.dicomX,
          startDicomY = result.dicomY
        )
        is Result.MouseMove -> copy(
          startDicomX = result.dicomX - startDicomX,
          startDicomY = result.dicomY - startDicomY
        )
        Result.Idle -> copy(
          startDicomX = 0.0,
          startDicomY = 0.0,
          isContrastBrightness = false,
        )
      }
  }

}