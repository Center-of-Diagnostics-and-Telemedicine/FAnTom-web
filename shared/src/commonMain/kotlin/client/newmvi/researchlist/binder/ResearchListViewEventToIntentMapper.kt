package client.newmvi.researchlist.binder

import client.newmvi.researchlist.store.ResearchListStore
import client.newmvi.researchlist.view.ResearchListView

object ResearchListViewEventToIntentMapper {

  operator fun invoke(event: ResearchListView.Event): ResearchListStore.Intent =
    when (event) {
      is ResearchListView.Event.ErrorShown -> ResearchListStore.Intent.DismissError
      is ResearchListView.Event.Init -> ResearchListStore.Intent.Init
    }
}