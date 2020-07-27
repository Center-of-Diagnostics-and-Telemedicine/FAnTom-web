package local

import com.badoo.reaktive.utils.atomic.AtomicReference
import com.badoo.reaktive.utils.atomic.update
import model.MarkEntity
import repository.MarksLocal

actual object MarksLocalDataSource : MarksLocal {

  private val map = AtomicReference<Map<Int, MarkEntity>>(emptyMap())

  override suspend fun get(markId: Int): MarkEntity? = map.value[markId]

  override suspend fun getAll(): List<MarkEntity> = map.value.values.toList()

  override suspend fun create(mark: MarkEntity) {
    val id = mark.id
    map.update { it.plus(id to mark) }
  }

  override suspend fun update(mark: MarkEntity) {
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

  override suspend fun saveList(marks: List<MarkEntity>) {
    map.update { map ->
      map.plus(marks.map { it.id to it })
    }
  }

  override suspend fun delete(markId: Int) {
    map.update { it.minus(markId) }
  }

  override suspend fun clean() {
    map.compareAndSet(map.value, emptyMap())
  }

}
