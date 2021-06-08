package local

import com.badoo.reaktive.utils.atomic.AtomicReference
import com.badoo.reaktive.utils.atomic.update
import model.Research
import repository.ResearchLocal

actual object ResearchLocalDataSource : ResearchLocal {

  private val map = AtomicReference<Map<String, Research>>(emptyMap())

  override suspend fun getAll(): List<Research> = map.value.values.toList()

  override suspend fun get(researchId: Int): Research = map.value[researchId.toString()]!!

  override suspend fun save(research: Research) {
    val id = research.id.toString()
    map.update {
      it.plus(
        id to requireNotNull(it[id]).copy(
          id = research.id,
          name = research.name,
          seen = research.seen,
          done = research.done,
          marked = research.marked
        )
      )
    }
  }

  override suspend fun saveList(list: List<Research>) {
    map.update { map ->
      map.plus(list.map { it.id.toString() to it })
    }
  }

  override suspend fun delete(id: String) {
    map.update { it.minus(id) }
  }
}