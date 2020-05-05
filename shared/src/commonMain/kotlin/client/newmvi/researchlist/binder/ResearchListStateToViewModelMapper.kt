package client.newmvi.researchlist.binder

import client.newmvi.researchlist.store.ResearchListStore
import client.newmvi.researchlist.view.ResearchListView

object ResearchListStateToViewModelMapper {

  operator fun invoke(state: ResearchListStore.State): ResearchListView.ResearchListViewModel =
    ResearchListView.ResearchListViewModel(
      isLoading = state.isLoading,
      error = state.error,
      data = state.data
    )
}