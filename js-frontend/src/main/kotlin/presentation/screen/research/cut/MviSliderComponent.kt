package presentation.screen.research.cut

import client.newmvi.slider.binder.SliderBinder
import client.newmvi.slider.view.SliderView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.MSliderOrientation
import com.ccfraser.muirwik.components.MSliderValueLabelDisplay
import com.ccfraser.muirwik.components.mSlider
import presentation.di.injectSlider
import react.*

class MviSliderComponent(props: MviSliderProps) : RComponent<MviSliderProps, MviSliderState>(props),
  SliderView {

  override val events: PublishSubject<SliderView.Event> = PublishSubject()
  private lateinit var binder: SliderBinder

  init {
    state = MviSliderState(props.maxValue / 2)
  }

  override fun MviSliderState.init(props: MviSliderProps) {
    binder = injectSlider(props.cutType)
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
  }

  override fun componentDidUpdate(
    prevProps: MviSliderProps,
    prevState: MviSliderState,
    snapshot: Any
  ) {
    if (prevProps.cutType != props.cutType) {
      binder.detachView()
      binder.onStop()
      binder = injectSlider(props.cutType)
      binder.attachView(this)
      binder.onStart()
    }
  }

  override fun show(model: SliderView.SliderViewModel) {
    setState {
      term = model.value
    }
  }

  override fun RBuilder.render() {
    mSlider(
      value = state.term,
      defaultValue = props.maxValue / 2,
      min = 1,
      max = props.maxValue,
      orientation = MSliderOrientation.horizontal,
      valueLabelDisplay = MSliderValueLabelDisplay.auto,
      onChange = { _, newValue -> handleEvent(newValue) }
    )
  }

  private fun handleEvent(value: Number) {
    if (value != state.term) {
      dispatch(SliderView.Event.Drag(value.toInt()))
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: SliderView.Event) {
    events.onNext(event)
  }
}

interface MviSliderProps : RProps {
  var cutType: Int
  var maxValue: Int
}

class MviSliderState(
  var term: Int
) : RState

fun RBuilder.mviSlider(
  cutType: Int,
  maxValue: Int
) = child(MviSliderComponent::class) {
  attrs.cutType = cutType
  attrs.maxValue = maxValue
}