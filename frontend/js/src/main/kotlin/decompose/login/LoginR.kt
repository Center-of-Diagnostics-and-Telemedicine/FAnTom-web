package decompose.login

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonSize
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import components.login.Login
import decompose.RenderableComponent
import decompose.login.LoginR.State
import kotlinx.css.*
import kotlinx.html.InputType
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import react.RBuilder
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import styled.styledForm

class LoginR(props: Props<Login>) : RenderableComponent<Login, State>(
  props = props,
  initialState = State(model = props.component.models.value)
) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mCssBaseline()

    val model = state.model

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
              fullWidth = true
            ) {
              attrs {
                required = true
                onChange = ::onLoginChanged
                inputLabelProps = object : RProps {
                  val shrink = true
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

  private fun auth(mouseEvent: MouseEvent) = component.auth()

  private fun onPasswordChanged(event: Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    component.onPasswordChanged(searchTerm)
  }

  private fun onLoginChanged(event: Event) {
    val target = event.target as HTMLInputElement
    val searchTerm = target.value
    component.onLoginChanged(searchTerm)
  }

  class State(
    var model: Login.Model,
  ) : RState
}