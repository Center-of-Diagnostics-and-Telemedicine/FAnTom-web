package client.newmvi.menu.table.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.AreaType
import model.Mark

interface TableStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val areas: List<Mark> = listOf(),
    val selectedAreaId: Int = -1
  )

  sealed class Intent {
    data class Delete(val selectedArea: Mark) : Intent()
    class Areas(val areas: List<Mark>) : Intent()
    class SelectArea(val areaId: Int) : Intent()
    class NewSelectedAreaIdIncome(val areaId: Int) : Intent()
    class ChangeMarkType(val id: Int,val  type: AreaType) : Intent()
    class ChangeComment(val id: Int, val comment: String) : Intent()
  }
}
