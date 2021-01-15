package client.newmvi.draw.binder

import client.newmvi.draw.store.DrawStore
import client.newmvi.draw.view.DrawView
import model.Circle

object DrawStateToViewModelMapper {

  operator fun invoke(state: DrawStore.State): DrawView.DrawViewModel {
    return if (state.radius != .0) {
      DrawView.DrawViewModel(Circle(state.startX, state.startY, state.radius))
    } else {
      DrawView.DrawViewModel()
    }
  }
}