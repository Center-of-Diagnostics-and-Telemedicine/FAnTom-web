package client.newmvi.menu.mipmethod.binder

import model.*
import client.newmvi.menu.mipmethod.store.MipMethodStore
import client.newmvi.menu.mipmethod.view.MipMethodView

object MipMethodStateToViewModelMapper {

  operator fun invoke(state: MipMethodStore.State): MipMethodView.MipMethodViewModel =
    MipMethodView.MipMethodViewModel(
      isLoading = state.isLoading,
      error = state.error,
      value = getName(state.value)
    )

  private fun getName(value: Int): String {
    return when (value) {
      MIP_METHOD_TYPE_AVERAGE -> AVERAGE
      MIP_METHOD_TYPE_MAXVALUE -> MAXVALUE
      else -> NO_MIP
    }
  }
}