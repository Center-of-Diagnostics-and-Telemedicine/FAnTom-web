package components.mip

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.HasIntValue
import model.Mip
import repository.MyMipRepository
import store.tools.MipStore
import store.tools.MipStore.*

internal class MipStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val mipRepository: MyMipRepository
) {

  fun provide(): MipStore =
    object : MipStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MipStore",
      initialState = State(
        list = listOf(Mip.No, Mip.Average(), Mip.Max()),
        current = Mip.No,
        currentValue = null
      ),
//      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class Loaded(val list: List<Mip>, val current: Mip = Mip.No) : Result()
    data class MipMethodChanged(val mip: Mip) : Result()
    data class MipValueChanged(val value: Int) : Result()
  }

  private fun getInitialState(): State = State(
    list = listOf(Mip.No, Mip.Average(), Mip.Max()),
    current = Mip.initial,
    currentValue = null
  )

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {
//    override fun executeAction(action: Unit, getState: () -> State) = load()

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleMipClick -> changeMip(intent.mip)
        is Intent.HandleMipValueChanged -> changeMipValue(intent.value)
      }.let {}
    }

    private fun changeMip(mip: Mip) {
      dispatch(Result.MipMethodChanged(mip))
      publish(Label.MipMethodChanged(mip))
    }

    private fun changeMipValue(value: Int) {
      dispatch(Result.MipValueChanged(value))
      publish(Label.MipValueChanged(value))
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(list = result.list, current = result.current)
        is Result.MipMethodChanged -> copy(
          current = result.mip,
          currentValue = if (result.mip is HasIntValue) result.mip.value else null
        )
        is Result.MipValueChanged -> copy(currentValue = result.value)
      }
  }
}