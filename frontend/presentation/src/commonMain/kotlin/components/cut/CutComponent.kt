package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.badoo.reaktive.observable.mapNotNull
import components.asValue
import components.cut.Cut.Dependencies
import components.getStore


class CutComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Cut, ComponentContext by componentContext, Dependencies by dependencies {

  val store = instanceKeeper.getStore {
    MyCutStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      researchRepository = researchRepository,
      plane = plane
    ).provide()
  }

  override val model: Value<Cut.Model> = store.asValue().map(stateToModel)

  init {
    bind(lifecycle = lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      cutInput.mapNotNull(inputToIntent) bindTo store
    }
  }

}