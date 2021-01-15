package client.newmvi.researchmvi.binder

import client.newmvi.researchmvi.store.ResearchStore
import client.newmvi.researchmvi.view.ResearchView

object ResearchViewEventToIntentMapper {

  operator fun invoke(event: ResearchView.Event): ResearchStore.Intent =
    when (event) {
      is ResearchView.Event.ErrorShown -> ResearchStore.Intent.DismissError
      is ResearchView.Event.Init -> ResearchStore.Intent.Init(event.researchId)
      is ResearchView.Event.Delete -> ResearchStore.Intent.DeleteCalled
      is ResearchView.Event.GridChanged -> ResearchStore.Intent.ChangeGrid(event.type)
      ResearchView.Event.Clear -> ResearchStore.Intent.Clear
      is ResearchView.Event.CellFullMode -> ResearchStore.Intent.OpenCell(event.cellModel)
      ResearchView.Event.Back -> ResearchStore.Intent.CallBackToResearchList
//      ResearchView.Event.Close -> ResearchStore.Intent.CallToCloseResearch
      is ResearchView.Event.CTTypeChosen -> ResearchStore.Intent.CTTypeChosen(event.ctType)
      is ResearchView.Event.ConfirmCtType -> ResearchStore.Intent.ConfirmCTType(
        event.ctType,
        event.leftPercent.toInt(),
        event.rightPercent.toInt()
      )
    }
}