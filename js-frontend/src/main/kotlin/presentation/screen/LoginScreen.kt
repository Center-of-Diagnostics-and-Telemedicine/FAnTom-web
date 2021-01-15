package presentation.screen

import client.newmvi.login.view.LoginView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.*
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import debugLog
import kotlinx.css.*
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import presentation.di.injectLogin
import presentation.screen.viewcomponents.alert
import react.*
import styled.*

class LoginScreen(props: LoginProps) :
  RComponent<LoginProps, LoginState>(props), LoginView {

  init {
    state = LoginState()
  }

  private val binder = injectLogin()
  override val events: PublishSubject<LoginView.Event> = PublishSubject()

  override fun show(model: LoginView.LoginViewModel) {
    if (model.error.isNotEmpty()) debugLog(model.error)
    if (model.authorized) {
      props.authorized()
    } else {
      setState {
        loading = model.isLoading
        error = model.error
      }
    }
  }

  override fun componentDidMount() {
    binder.attachView(this@LoginScreen)
    binder.onStart()
    dispatch(LoginView.Event.Init)
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      alert(
        message = state.error,
        open = state.error.isNotEmpty(),
        handleClose = { dispatch(LoginView.Event.ErrorShown) }
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
            }
            mTextField(
              variant = MFormControlVariant.outlined,
              margin = MFormControlMargin.normal,
              id = "login",
              label = "Логин",
              name = "login",
              autoComplete = "login",
              autoFocus = true,
              error = state.loginError,
              fullWidth = true
            ) {
              attrs {
                required = true
                onChange = ::onLoginChanged
                inputLabelProps = object : RProps {
                  val shrink = true
                }
                onFocus = ::onFocus
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
              error = state.passwordError,
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
                disabled = state.loading,
                variant = MButtonVariant.contained,
                color = MColor.primary
              ) {
                attrs {
                  fullWidth = true
                  onClick = {
                    dispatch(LoginView.Event.Auth(state.login, state.password))
                  }
                }
              }
              if (state.loading) {
                mCircularProgress(size = 24.px) {
                  css {
                    color = Colors.Blue.shade500
                    position = Position.absolute
                    top = 50.pct
                    left = 50.pct
                    marginTop = -12.px
                    marginLeft = -12.px
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private fun onPasswordChanged(event: Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    setState {
      password = searchTerm
    }
  }

  private fun onLoginChanged(event: Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    setState {
      login = searchTerm
    }
  }

  private fun onFocus(event: Event) {
    if (state.loginError || state.passwordError) {
      dispatch(LoginView.Event.ErrorShown)
    }
  }

  override fun componentWillUnmount() {
    binder.detachView()
    binder.onStop()
  }

  override fun dispatch(event: LoginView.Event) {
    events.onNext(event)
  }


}

class LoginState : RState {
  var loading: Boolean = true
  var error: String = ""
  var login: String = ""
  var password: String = ""
  var loginError: Boolean = false
  var passwordError: Boolean = false
}


interface LoginProps : RProps {
  var authorized: () -> Unit
}

fun RBuilder.login(authListener: () -> Unit) = child(LoginScreen::class) {
  attrs.authorized = authListener
}