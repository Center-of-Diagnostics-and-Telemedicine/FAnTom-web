import model.*
import repository.ResearchRepository

class TestResearchRepository : ResearchRepository {

  override val token: suspend () -> String = { testToken }

  override suspend fun getResearches(): List<Research> {
    return testResearches
  }

  override suspend fun getFiltered(filter: Filter): List<Research> {
    return when (filter) {
      Filter.All -> testResearches
      Filter.NotSeen -> testResearches.filter { it.seen.not() }
      Filter.Seen -> testResearches.filter { it.seen }
      Filter.Done -> testResearches.filter { it.done }
    }
  }

  override suspend fun initResearch(researchId: Int): ResearchSlicesSizesData {
    TODO("Not yet implemented")
  }

  override suspend fun getSlice(
    researchId: Int,
    black: Int,
    white: Int,
    gamma: Double,
    type: Int,
    mipMethod: Int,
    sliceNumber: Int,
    aproxSize: Int
  ): String {
    TODO("Not yet implemented")
  }

  override suspend fun getHounsfieldData(
    sliceNumber: Int,
    type: Int,
    mipMethod: Int,
    mipValue: Int,
    horizontal: Int,
    vertical: Int
  ): Double {
    TODO("Not yet implemented")
  }

  override suspend fun confirmCtTypeForResearch(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  ) {
    TODO("Not yet implemented")
  }

  override suspend fun closeSession(researchId: Int) {
    TODO("Not yet implemented")
  }
}
