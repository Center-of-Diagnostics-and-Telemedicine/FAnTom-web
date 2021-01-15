package presentation.screen.research.menu

import client.newmvi.menu.table.view.TableView
import com.badoo.reaktive.subject.publish.PublishSubject
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.table.*
import com.ccfraser.muirwik.components.themeContext
import model.Mark
import org.w3c.dom.Node
import react.*

class MviResearchTableComponent(props: MviResearchTableProps) :
  RComponent<MviResearchTableProps, MviResearchTableState>(props),
  TableView {

  override val events: PublishSubject<TableView.Event> = PublishSubject()
//  private val binder: TableBinder = injectTable()
  private var anchorElement: Node? = null

  init {
    state = MviResearchTableState(listOf(), -1, -1)
  }

  override fun show(model: TableView.TableViewModel) {
    setState {
      areas = model.areas
      selectedAreaId = model.selectedAreaId
    }
  }

  override fun componentDidMount() {
//    binder.detachView()
//    binder.onStop()
//    binder.attachView(this)
//    binder.onStart()
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      mPaper {
        mTable {
          mTableHead {
            mTableRow {
              mTableCell(align = MTableCellAlign.center) { +"X" }
              mTableCell(align = MTableCellAlign.center) { +"Y" }
              mTableCell(align = MTableCellAlign.center) { +"Z" }
              mTableCell(align = MTableCellAlign.center) { +"mm" }
              mTableCell(align = MTableCellAlign.center) { +"Тип" }
              mTableCell(align = MTableCellAlign.center) { }
            }
          }
          mTableBody {
            state.areas.forEach { area ->
              researchTableRow(
                selected = state.selectedAreaId == area.id,
                area = area,
                select = { dispatch(TableView.Event.Select(area)) },
                changeType = { type: Int ->
                  dispatch(TableView.Event.ChangeMarkType(area.id, type))
                },
                delete = { dispatch(TableView.Event.Delete(area)) },
                onCommentChanged = { comment: String ->
                  dispatch(TableView.Event.ChangeComment(area.id, comment))
                }
              )
            }
          }
        }
      }
    }
  }

  override fun componentWillUnmount() {
//    binder.detachView()
//    binder.onStop()
  }

  override fun dispatch(event: TableView.Event) {
    events.onNext(event)
  }
}

interface MviResearchTableProps : RProps

class MviResearchTableState(
  var areas: List<Mark>,
  var selectedAreaId: Int,
  var selectedMenuIndex: Int
) : RState

fun RBuilder.mviResearchTable() = child(MviResearchTableComponent::class) {}
