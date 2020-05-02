package presentation.screen.research.menu

import client.newmvi.menu.mipmethod.binder.MipMethodBinder
import client.newmvi.menu.mipmethod.view.MipMethodView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.mRadio
import com.ccfraser.muirwik.components.mRadioGroup
import presentation.di.injectMipMethod
import kotlinx.css.Display
import kotlinx.css.display
import model.*
import react.*
import styled.css

class MviMipMethodComponent(props: MviMipMethodProps) :
  RComponent<MviMipMethodProps, MviMipMethodState>(props),
  MipMethodView {

  override val events: PublishSubject<MipMethodView.Event> = PublishSubject()
  private val binder: MipMethodBinder = injectMipMethod()

  init {
    state = MviMipMethodState(NO_MIP)
  }

  override fun show(model: MipMethodView.MipMethodViewModel) {
    setState {
      radioValue = model.value
    }
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
  }

  override fun RBuilder.render() {
    val altBuilder = RBuilder()
    mRadioGroup(
      value = state.radioValue,
      onChange = { _, value -> dispatch(MipMethodView.Event.ChangeMethod(value)) }) {
      css { display = Display.inlineFlex }
      mFormControlLabel("Среднее", value = AVERAGE, control = altBuilder.mRadio())
      mFormControlLabel("Максимальное значение", value = MAXVALUE, control = altBuilder.mRadio())
      mFormControlLabel("Без MIP", value = NO_MIP, control = altBuilder.mRadio())
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: MipMethodView.Event) {
    events.onNext(event)
  }
}

interface MviMipMethodProps : RProps

class MviMipMethodState(
  var radioValue: String
) : RState


fun RBuilder.mviMipMethod() = child(MviMipMethodComponent::class) {}