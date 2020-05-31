package mapper

import controller.GridContainerController.Input
import store.gridcontainer.GridContainerStore.Intent
import store.gridcontainer.GridContainerStore.State
import view.GridContainerView.Model

val gridContainerStateToGridContainerModel: State.() -> Model? = {
  Model(
    items = cuts,
    grid = grid
  )
}

val inputToGridContainerIntent: Input.() -> Intent = {
  when (this) {
    is Input.GridChanged -> Intent.HandleGridChanged(grid)
  }
}

//val gridContainerEventToGridContainerIntent: Event.() -> Intent? = {
//  when (this) {
//    Event.Close -> Intent.
//  }
//}


