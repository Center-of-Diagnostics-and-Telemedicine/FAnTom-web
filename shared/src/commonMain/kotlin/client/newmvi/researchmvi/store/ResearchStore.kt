package client.newmvi.researchmvi.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.*

interface ResearchStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: ResearchSlicesSizesData? = null,
    val researchId: Int = 0,
    val gridModel: GridModel,
    val studyCompleted: Boolean = false,
    val oldGridModel: GridModel? = null,
    val ctTypeToConfirm: CTType? = null
  )

  sealed class Intent {
    data class Init(val researchId: Int) : Intent()
    object Clear : Intent()
    class DeleteMark(val areaId: Int) : Intent()
    object DeleteCalled : Intent()
    class SaveMark(val areaToSave: AreaToSave) : Intent()
    class UpdateMark(val areaToUpdate: SelectedArea) : Intent()
    class ChangeGrid(val type: CutsGridType) : Intent()
    class ChangeCutType(val newCutType: ChangeCutTypeModel) : Intent()
    class OpenCell(val cellModel: CellModel) : Intent()
    class CTTypeChosen(val ctType: CTType) : ResearchStore.Intent()
    class ConfirmCTType(val ctType: CTType, val leftPercent: Int, val rightPercent: Int) : Intent()

    object CloseSession : ResearchStore.Intent()

    object CallToCloseResearch : Intent()
    object CallBackToResearchList : Intent()

    object Close : Intent()
    object ShowAreasNotFull : Intent()
    object BackToResearchList : Intent()

    object DismissError : Intent()
  }
}