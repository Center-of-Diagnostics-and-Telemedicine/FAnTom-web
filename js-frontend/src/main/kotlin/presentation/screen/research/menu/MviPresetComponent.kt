package presentation.screen.research.menu

import client.*
import client.newmvi.menu.preset.binder.PresetBinder
import client.newmvi.menu.preset.view.PresetView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.form.MFormControlComponent
import com.ccfraser.muirwik.components.form.mFormControl
import com.ccfraser.muirwik.components.form.mFormControlLabel
import com.ccfraser.muirwik.components.mRadio
import com.ccfraser.muirwik.components.mRadioGroup
import presentation.di.injectPreset
import kotlinx.css.Display
import kotlinx.css.display
import model.*
import react.*
import styled.css

class MviPresetComponent(props: MviPresetProps) : RComponent<MviPresetProps, MviPresetState>(props),
  PresetView {

  override val events: PublishSubject<PresetView.Event> = PublishSubject()
  private val binder: PresetBinder = injectPreset()

  init {
    state = MviPresetState(LUNGS)
  }

  override fun show(model: PresetView.PresetViewModel) {
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
    mFormControl(component = MFormControlComponent.fieldSet) {
      mRadioGroup(
        value = state.radioValue,
        onChange = { _, value -> dispatch(PresetView.Event.ChangeValue(value)) }) {
        css { display = Display.inlineFlex }
        mFormControlLabel("Мягкие ткани", value = SOFT_TISSUE, control = altBuilder.mRadio())
        mFormControlLabel("Сосуды", value = VESSELS, control = altBuilder.mRadio())
        mFormControlLabel("Кости", value = BONES, control = altBuilder.mRadio())
        mFormControlLabel("Мозг", value = BRAIN, control = altBuilder.mRadio())
        mFormControlLabel("Лёгкие", value = LUNGS, control = altBuilder.mRadio())
      }
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: PresetView.Event) {
    events.onNext(event)
  }
}

class MviPresetState(var radioValue: String) : RState

interface MviPresetProps : RProps

fun RBuilder.mviPreset() = child(MviPresetComponent::class) {}