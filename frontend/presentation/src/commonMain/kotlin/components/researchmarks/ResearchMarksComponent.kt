package components.researchmarks

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import components.asValue
import components.getStore
import components.researchmarks.ResearchMarks.Dependencies
import components.researchmarks.ResearchMarks.Model
import store.marks.MyMarksStore.Intent

internal class ResearchMarksComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : ResearchMarks, ComponentContext by componentContext, Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      ResearchMarksStoreProvider(
        storeFactory = storeFactory,
        researchRepository = researchRepository,
        marksRepository = marksRepository,
        researchId = researchId,
        data = data
      ).provide()
    }

  override val model: Value<Model> = store.asValue().map(stateToModel)

  override fun onSelectItem(id: Int) {
    store.accept(Intent.SelectMark(id = id))
  }

  override fun onChangeComment(id: Int, comment: String) {
    store.accept(Intent.ChangeComment(id = id, comment = comment))
  }

  override fun onChangeMarkType(id: Int, typeId: String) {
    store.accept(Intent.ChangeMarkType(id = id, typeId = typeId))
  }

  override fun onChangeVisibility(id: Int) {
    store.accept(Intent.ChangeVisibility(id = id))
  }

  override fun onDeleteItem(id: Int) {
    store.accept(Intent.DeleteMark(id))
  }
}