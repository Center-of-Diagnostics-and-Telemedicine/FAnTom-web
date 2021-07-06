package components.cut

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import components.asValue
import components.cut.Cut.Dependencies
import components.cut.Cut.Input
import components.getStore


class CutComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Cut, ComponentContext by componentContext, Dependencies by dependencies {

  private val inputRelay: Relay<Input> = PublishSubject()
  override val input: Consumer<Input> = inputRelay

  val store = instanceKeeper.getStore {
    MyCutStoreProvider(
      storeFactory = storeFactory,
      brightnessRepository = brightnessRepository,
      mipRepository = mipRepository,
      researchId = researchId,
      researchRepository = researchRepository,
      plane = plane
    ).provide()
  }

  override val model: Value<Cut.Model> = store.asValue().map(stateToModel)

  init {
    inputRelay.mapNotNull(inputToIntent).subscribe(onNext = store::accept)
  }

}