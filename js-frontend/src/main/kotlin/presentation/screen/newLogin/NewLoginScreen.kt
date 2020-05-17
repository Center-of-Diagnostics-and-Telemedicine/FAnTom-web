package presentation.screen.newLogin

import client.newmvi.newlogin.controller.LoginDependencies
import client.newmvi.newlogin.controller.NewLoginController
import client.newmvi.newlogin.view.NewLoginView.Event
import client.newmvi.newlogin.view.NewLoginView.Model
import client.newmvi.newlogin.view.initialModel
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import debugLog
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import presentation.di.loginRepository
import presentation.screen.viewcomponents.alert
import react.*
import styled.css
import styled.styledDiv
import styled.styledForm

class NewLoginScreen(props: NewLoginProps) :
  RComponent<NewLoginProps, NewLoginState>(props) {

  private val loginViewDelegate = NewLoginViewImpl(updateState = ::updateState)
  private val lifecycleWrapper = LifecycleWrapper()
  private val controller = NewLoginController(
    dependencies = LoginDependencies(
      storeFactory = DefaultStoreFactory,
      lifecycle = lifecycleWrapper.lifecycle,
      repository = loginRepository
    )
  )

  init {
    state = NewLoginState(initialModel())
  }

  override fun componentWillMount() {
    lifecycleWrapper.start()
  }

  override fun componentDidMount() {
    controller.onViewCreated(loginViewDelegate, lifecycleWrapper.lifecycle, output = {})
  }

  private fun updateState(newModel: Model) {
    setState { model = newModel }
  }

  override fun RBuilder.render() {
    val model = state.model

    if (model.authorized) {
      debugLog("authorized")
      props.authorized()
      return
    }

    themeContext.Consumer { theme ->
      alert(
        message = model.error,
        open = model.error.isNotEmpty(),
        handleClose = ::dismissError
      )
      mContainer {
        attrs {
          component = "main"
          maxWidth = "xs"
        }
        mCssBaseline()
        styledDiv {
          css {
            marginTop = 9.spacingUnits
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
          }
          mAvatar {
            css {
              margin(1.spacingUnits)
              backgroundColor = Color(theme.palette.secondary.main)
            }
            mIcon("lock_open")
          }
          mTypography {
            attrs {
              component = "h1"
              variant = MTypographyVariant.h5
            }
            +"Авторизация"
          }
          styledForm {
            css {
              width = 100.pct
              marginTop = 1.spacingUnits
            }
            attrs {
              novalidate = true
              onSubmitFunction = ::auth
            }
            mTextField(
              variant = MFormControlVariant.outlined,
              margin = MFormControlMargin.normal,
              id = "login",
              label = "Логин",
              name = "login",
              autoComplete = "login",
              autoFocus = true,
              fullWidth = true
            ) {
              attrs {
                required = true
                onChange = ::onLoginChanged
                inputLabelProps = object : RProps {
                  val shrink = true
                }
                onFocus = { dismissError() }
              }
            }
            mTextField(
              variant = MFormControlVariant.outlined,
              margin = MFormControlMargin.normal,
              id = "password",
              label = "Пароль",
              name = "password",
              autoComplete = "current-password",
              type = InputType.password,
              fullWidth = true
            ) {
              attrs {
                required = true
                onChange = ::onPasswordChanged
                inputLabelProps = object : RProps {
                  val shrink = true
                }
              }
            }
            styledDiv {
              css {
                margin(3.spacingUnits, 0.spacingUnits, 2.spacingUnits)
                position = Position.relative
              }
              mButton(
                "Войти",
                size = MButtonSize.large,
                disabled = model.loading,
                variant = MButtonVariant.contained,
                color = MColor.primary
              ) {
                attrs {
                  fullWidth = true
                  onClick = ::auth
                }
              }
              if (model.loading) {
                mCircularProgress(size = 24.px) {
                  css {
                    color = Colors.Blue.shade500
                    position = Position.absolute
                    top = 50.pct
                    left = 50.pct
                    marginTop = (-12).px
                    marginLeft = (-12).px
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private fun dismissError() {
    loginViewDelegate.dispatchEvent(Event.DismissError)
  }

  private fun auth(event: org.w3c.dom.events.Event) {
    state.model.let { loginViewDelegate.dispatchEvent(Event.Auth(it.login, it.password)) }
  }

  private fun onPasswordChanged(event: org.w3c.dom.events.Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    loginViewDelegate.dispatchEvent(Event.PasswordChanged(searchTerm))
  }

  private fun onLoginChanged(event: org.w3c.dom.events.Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    loginViewDelegate.dispatchEvent(Event.LoginChanged(searchTerm))
  }

  override fun componentWillUnmount() {
    lifecycleWrapper.stop()
  }
}

class NewLoginState(var model: Model) : RState

interface NewLoginProps : RProps {
  var authorized: () -> Unit
}

fun RBuilder.newLogin(authorized: () -> Unit) = child(NewLoginScreen::class) {
  attrs.authorized = authorized
}