package client.newmvi.menu.mipmethod.binder

import model.*
import client.newmvi.menu.mipmethod.store.MipMethodStore
import client.newmvi.menu.mipmethod.view.MipMethodView

object MipMethodViewEventToIntentMapper {

  operator fun invoke(event: MipMethodView.Event): MipMethodStore.Intent =
    when (event) {
      is MipMethodView.Event.ChangeMethod -> MipMethodStore.Intent.ChangeMethod(
        getMethod(event.value)
      )
    }

  private fun getMethod(value: String): Int {
    return when (value) {
      AVERAGE -> MIP_METHOD_TYPE_AVERAGE
      MAXVALUE -> MIP_METHOD_TYPE_MAXVALUE
      else -> MIP_METHOD_TYPE_NO_MIP
    }

  }
}