package client.newmvi.menu.table.binder

import client.newmvi.menu.table.store.TableStore
import client.newmvi.menu.table.view.TableView

internal object TableStateToViewModelMapper {

  operator fun invoke(state: TableStore.State): TableView.TableViewModel =
    TableView.TableViewModel(
      isLoading = state.isLoading,
      error = state.error,
      areas = state.areas,
      selectedAreaId = state.selectedAreaId
    )
}