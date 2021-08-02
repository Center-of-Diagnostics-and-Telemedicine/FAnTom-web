package repository

import com.badoo.reaktive.observable.Observable
import model.MarkData
import model.MarkEntity

interface MyMarksRepository {

  val token: suspend () -> String

  val mark: Observable<MarkEntity?>
  val marks: Observable<List<MarkEntity>?>

  suspend fun setMark(mark: MarkEntity?)
  suspend fun loadMarks(researchId: Int)
  suspend fun saveMark(markToSave: MarkData, researchId: Int)
  suspend fun updateMark(mark: MarkEntity, researchId: Int)
  suspend fun deleteMark(id: Int, researchId: Int)
}