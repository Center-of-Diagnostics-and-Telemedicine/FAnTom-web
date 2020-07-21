package mapper

import controller.CutsContainerController.Input
import store.gridcontainer.CutsContainerStore.Intent
import store.gridcontainer.CutsContainerStore.State
import view.CutsContainerView.Model

val CutsContainerStateToCutsContainerModel: State.() -> Model? = {
  Model(
    items = cuts,
    grid = grid
  )
}

val inputToCutsContainerIntent: Input.() -> Intent? = {
  when (this) {
    is Input.GridChanged -> Intent.HandleGridChanged(grid)
    is Input.ChangeCutType -> Intent.HandleChangeCutType(cutType, cut)
    Input.Idle -> null
  }
}

//val gridContainerEventToGridContainerIntent: Event.() -> Intent? = {
//  when (this) {
//    Event.Close -> Intent.
//  }
//}


