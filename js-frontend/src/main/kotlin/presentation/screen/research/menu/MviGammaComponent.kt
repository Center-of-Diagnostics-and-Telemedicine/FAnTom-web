package presentation.screen.research.menu

import client.newmvi.menu.gamma.binder.GammaBinder
import client.newmvi.menu.gamma.view.GammaView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.mSlider
import com.ccfraser.muirwik.components.mTypography
import presentation.di.injectGamma
import react.*

class MviGammaComponent(props: MviGammaProps) : RComponent<MviGammaProps, MviGammaState>(props),
  GammaView {

  override val events: PublishSubject<GammaView.Event> = PublishSubject()
  private val binder: GammaBinder = injectGamma()

  override fun show(model: GammaView.GammaViewModel) {
    setState {
      term = model.value
    }
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
  }

  override fun RBuilder.render() {
    mTypography("Значение гамма")
    mSlider(
      value = state.term,
      defaultValue = 1,
      max = 3,
      min = 0.05,
      valueLabelDisplay = MSliderValueLabelDisplay.auto,
      step = 0.05,
      onChange = { _, newValue -> handleEvent(newValue) }
    )
  }

  private fun handleEvent(newValue: Number) {
    if (newValue != state.term) {
      dispatch(GammaView.Event.ChangeValue(newValue.toDouble()))
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: GammaView.Event) {
    events.onNext(event)
  }
}

interface MviGammaProps : RProps

interface MviGammaState : RState {
  var term: Number
}

fun RBuilder.mviGamma() = child(MviGammaComponent::class) {}