package client.newmvi.menu.table.binder

import model.AreaType
import client.newmvi.menu.table.store.TableStore
import client.newmvi.menu.table.view.TableView

internal object TableViewEventToIntentMapper {

  operator fun invoke(event: TableView.Event): TableStore.Intent =
    when (event) {
      is TableView.Event.Delete -> TableStore.Intent.Delete(event.value)
      is TableView.Event.Select -> TableStore.Intent.SelectArea(event.area.id)
      is TableView.Event.ChangeMarkType -> {
        val type = when (event.type) {
          0 -> AreaType.SOLID_NODULE
          1 -> AreaType.PART_SOLID_NODULE
          2 -> AreaType.PURE_SUBSOLID_NODULE
          3 -> AreaType.NOT_ONKO
          else -> throw NotImplementedError("Не добавил новый тип для AreaType, type = ${event.type}")
        }
        TableStore.Intent.ChangeMarkType(event.id, type)
      }
      is TableView.Event.ChangeComment -> TableStore.Intent.ChangeComment(event.id, event.comment)
    }
}