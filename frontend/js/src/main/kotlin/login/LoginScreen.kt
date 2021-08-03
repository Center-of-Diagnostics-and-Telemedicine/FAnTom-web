package login

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import components.alert
import controller.LoginController
import controller.LoginControllerImpl
import destroy
import kotlinext.js.jsObject
import kotlinx.css.*
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.*
import react.dom.attrs
import repository.LoginRepository
import resume
import styled.css
import styled.styledDiv
import styled.styledForm
import view.LoginView

class LoginScreen(
  prps: LoginScreenProps
) : RComponent<LoginScreenProps, LoginState>(prps) {

  private val viewDelegate = LoginViewProxy(updateState = ::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: LoginController

  init {
    state = LoginState(LoginView.Model("", "", "", loading = false))
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(viewDelegate, lifecycleRegistry)
  }

  private fun createController(): LoginController {
    val dependencies = props.dependencies
    val loginControllerDependencies =
      object : LoginController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return LoginControllerImpl(loginControllerDependencies)
  }

  private fun updateState(newModel: LoginView.Model) {
    setState { model = newModel }
  }

  override fun RBuilder.render() {
    mCssBaseline()
    val model = state.model

    val error = state.model.error
    alert(
      message = error,
      open = error.isNotEmpty(),
      handleClose = { viewDelegate.dispatch(LoginView.Event.DismissError) }
    )

    themeContext.Consumer { theme ->
      mContainer {
        attrs {
          component = "main"
          maxWidth = "xs"
        }
        styledDiv {
          css {
            paddingTop = 9.spacingUnits
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
          mTypography(
            text = "Авторизация",
            component = "h1",
            variant = MTypographyVariant.h5
          )
          styledForm {
            css {
              width = 100.pct
              marginTop = 1.spacingUnits
            }
            this@styledForm.attrs {
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
              fullWidth = true
            ) {
              attrs {
                required = true
                onChange = ::onLoginChanged
                inputLabelProps = jsObject {
                  this.asDynamic().shrink = true
                }
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
                inputLabelProps = jsObject {
                  this.asDynamic().shrink = true
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

  private fun auth(mouseEvent: MouseEvent) {
    viewDelegate.dispatch(LoginView.Event.Auth)
  }

  private fun onPasswordChanged(event: Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    viewDelegate.dispatch(LoginView.Event.PasswordChanged(searchTerm))
  }

  private fun onLoginChanged(event: Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    viewDelegate.dispatch(LoginView.Event.LoginChanged(searchTerm))
  }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val loginRepository: LoginRepository
    val loginOutput: (LoginController.Output) -> Unit
  }

}

interface LoginScreenProps : RProps {
  var dependencies: LoginScreen.Dependencies
}

class LoginState(
  var model: LoginView.Model
) : RState

fun RBuilder.login(
  dependencies: LoginScreen.Dependencies
) =
  child(LoginScreen::class) {
    attrs.dependencies = dependencies
  }
