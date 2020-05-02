package presentation.screen.research.menu

import client.newmvi.menu.black.binder.BlackBinder
import client.newmvi.menu.black.view.BlackView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.mTextField
import com.ccfraser.muirwik.components.targetInputValue
import presentation.di.injectBlack
import kotlinx.html.InputType
import react.*

class MviBlackComponent(props: MviBlackProps) : RComponent<MviBlackProps, MviBlackState>(props),
  BlackView {

  override val events: PublishSubject<BlackView.Event> = PublishSubject()
  private val binder: BlackBinder = injectBlack()

  override fun show(model: BlackView.BlackViewModel) {
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
      label = "Значение черного",
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
      dispatch(BlackView.Event.ChangeValue(searchTerm.toDouble()))
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: BlackView.Event) {
    events.onNext(event)
  }
}

interface MviBlackProps : RProps

interface MviBlackState : RState {
  var term: String
}

fun RBuilder.mviBlack() = child(MviBlackComponent::class) {}