package local

import com.badoo.reaktive.utils.atomic.AtomicReference
import com.badoo.reaktive.utils.atomic.update
import model.Research
import repository.ResearchLocal

actual object ResearchLocalDataSource : ResearchLocal {

  private val map = AtomicReference<Map<String, Research>>(emptyMap())

  override suspend fun get(accessionName: String): Research? = map.value[accessionName]

  override suspend fun getAll(): List<Research> = map.value.values.toList()

  override suspend fun save(research: Research) {
    val name = research.name
    map.update {
      it.plus(
        name to requireNotNull(it[name]).copy(
          id = research.id,
          name = research.name,
          seen = research.seen,
          done = research.done,
          marked = research.marked,
          modality = research.modality
        )
      )
    }
  }

  override suspend fun saveList(list: List<Research>) {
    map.update { map ->
      map.plus(list.map { it.name to it })
    }
  }

  override suspend fun delete(name: String) {
    map.update { it.minus(name) }
  }
}