package client.newmvi.researchmvi.binder

import client.newmvi.researchmvi.store.ResearchStore
import model.CloseCommands

object CloseCommandToIntentMapper {

  fun invoke(closeCommand: CloseCommands): ResearchStore.Intent {
    return when(closeCommand){
      CloseCommands.ReadyToClose -> ResearchStore.Intent.Close
      CloseCommands.AreasNotFull -> ResearchStore.Intent.ShowAreasNotFull
      CloseCommands.BackToResearchList -> ResearchStore.Intent.BackToResearchList
    }
  }
}