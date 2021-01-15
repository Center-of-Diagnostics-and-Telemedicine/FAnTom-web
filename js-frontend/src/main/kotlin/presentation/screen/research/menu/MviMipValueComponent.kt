package presentation.screen.research.menu

import client.newmvi.menu.mipvalue.binder.MipValueBinder
import client.newmvi.menu.mipvalue.view.MipValueView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.mSlider
import presentation.di.injectMipValue
import react.*

class MviMipValueComponent(props: MviMipValueProps) :
  RComponent<MviMipValueProps, MviMipValueState>(props),
  MipValueView {

  override val events: PublishSubject<MipValueView.Event> = PublishSubject()
  private val binder: MipValueBinder = injectMipValue()

  override fun show(model: MipValueView.MipValueViewModel) {
    setState {
      term = model.value
      available = model.available
    }
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
  }

  override fun RBuilder.render() {
    if (state.available) {
      mSlider(
        value = state.term,
        max = 10,
        min = 0,
        valueLabelDisplay = MSliderValueLabelDisplay.auto,
        showMarks = true,
        step = 1,
        onChange = { _, newValue -> handleEvent(newValue) }
      )
    }
  }

  private fun handleEvent(newValue: Number) {
    if (newValue != state.term) {
      dispatch(MipValueView.Event.ChangeMethod(newValue.toInt()))
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: MipValueView.Event) {
    events.onNext(event)
  }
}

interface MviMipValueProps : RProps

interface MviMipValueState : RState {
  var term: Number
  var available: Boolean
}

fun RBuilder.mviMipValue() = child(MviMipValueComponent::class) {}