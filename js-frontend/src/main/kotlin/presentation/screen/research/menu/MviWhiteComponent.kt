package presentation.screen.research.menu

import client.newmvi.menu.white.binder.WhiteBinder
import client.newmvi.menu.white.view.WhiteView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.targetInputValue
import presentation.di.injectWhite
import kotlinx.html.InputType
import react.*

class MviWhiteComponent(props: MviWhiteProps) : RComponent<MviWhiteProps, MviWhiteState>(props),
  WhiteView {

  override val events: PublishSubject<WhiteView.Event> = PublishSubject()
  private val binder: WhiteBinder = injectWhite()

  override fun show(model: WhiteView.WhiteViewModel) {
    setState {
      term = model.value.toString()
    }
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
  }

  override fun RBuilder.render() {
    mTextField(
      label = "Значение белого",
      type = InputType.number,
      value = state.term,
      onChange = { val value = it.targetInputValue; handleEvent(value) },
      variant = MFormControlVariant.outlined
    ) {
      attrs.inputLabelProps = object : RProps {
        val shrink = true
      }
    }
  }

  private fun handleEvent(searchTerm: String) {

    if (searchTerm != state.term) {
      dispatch(WhiteView.Event.ChangeValue(searchTerm.toDouble()))
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: WhiteView.Event) {
    events.onNext(event)
  }
}

interface MviWhiteProps : RProps

interface MviWhiteState : RState {
  var term: String
}

fun RBuilder.mviWhite() = child(MviWhiteComponent::class) {}