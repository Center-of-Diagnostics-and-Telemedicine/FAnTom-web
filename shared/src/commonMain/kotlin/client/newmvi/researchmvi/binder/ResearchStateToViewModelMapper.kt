package client.newmvi.researchmvi.binder

import client.newmvi.researchmvi.store.ResearchStore
import client.newmvi.researchmvi.view.ResearchView

object ResearchStateToViewModelMapper {

  operator fun invoke(state: ResearchStore.State): ResearchView.ResearchViewModel =
    ResearchView.ResearchViewModel(
      isLoading = state.isLoading,
      error = state.error,
      data = state.data,
      gridModel = state.gridModel,
      studyCompleted = state.studyCompleted,
      ctTypeToConfirm = state.ctTypeToConfirm,
      sessionClosed = state.sessionClosed
    )
}