package local

import com.badoo.reaktive.utils.atomic.AtomicReference
import com.badoo.reaktive.utils.atomic.update
import model.MarkDomain
import repository.MarksLocal

actual object MarksLocalDataSource : MarksLocal {

  private val map = AtomicReference<Map<Int, MarkDomain>>(emptyMap())

  override suspend fun get(markId: Int): MarkDomain? = map.value[markId]

  override suspend fun getAll(): List<MarkDomain> = map.value.values.toList()

  override suspend fun create(mark: MarkDomain) {
    val id = mark.id
    map.update { it.plus(id to mark) }
  }

  override suspend fun update(mark: MarkDomain) {
    val id = mark.id
    map.update {
      it.plus(
        id to requireNotNull(it[id]).copy(
          markData = mark.markData,
          comment = mark.comment,
          type = mark.type,
        ).also { newMark -> newMark.selected = mark.selected }
      )
    }
  }

  override suspend fun saveList(marks: List<MarkDomain>) {
    map.update { map ->
      map.plus(marks.map { it.id to it })
    }
  }

  override suspend fun delete(markId: Int) {
    map.update { it.minus(markId) }
  }

}
