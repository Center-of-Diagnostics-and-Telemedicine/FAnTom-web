package presentation.screen.research.cut

import client.newmvi.cut.binder.CutBinder
import client.newmvi.cut.view.CutView
import com.badoo.reaktive.subject.publish.PublishSubject
import presentation.di.injectCut
import kotlinx.css.*
import kotlinx.css.properties.scaleY
import react.*
import styled.css
import styled.styledImg

class MviCutComponent(props: MviCutProps) : RComponent<MviCutProps, MviCutState>(props),
  CutView {

  override val events: PublishSubject<CutView.Event> = PublishSubject()
  private lateinit var binder: CutBinder

  override fun MviCutState.init(props: MviCutProps) {
    src = ""
    loading = false
    error = ""
    binder = injectCut(props.cutType)
  }

  override fun componentDidUpdate(prevProps: MviCutProps, prevState: MviCutState, snapshot: Any) {
    if(prevProps.cutType != props.cutType){
      binder.detachView()
      binder.onStop()
      binder = injectCut(props.cutType)
      binder.attachView(this)
      binder.onStart()
    }
  }

  override fun componentDidMount() {
    binder.detachView()
    binder.onStop()
    binder.attachView(this)
    binder.onStart()
  }


  override fun show(model: CutView.CutViewModel) {
    setState {
      loading = model.isLoading
      src = model.img
      error = model.error
    }
  }

  override fun RBuilder.render() {
    styledImg {
      css {
        transform.scaleY(-1)
        width = props.width.px
        height = props.height.px
        objectFit = ObjectFit.contain
      }
      if (state.src.isNotEmpty()) {
        attrs.src = "data:image/bmp;base64,${state.src}"
      }
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: CutView.Event) {
    events.onNext(event)
  }
}

interface MviCutProps : RProps {
  var cutType: Int
  var width: Int
  var height: Int
}

interface MviCutState : RState {
  var src: String
  var loading: Boolean
  var error: String
}

fun RBuilder.mviCut(
  cutType: Int,
  clientHeight: Int,
  clientWidth: Int
) = child(MviCutComponent::class) {
  attrs.cutType = cutType
  attrs.width = clientWidth
  attrs.height = clientHeight
}