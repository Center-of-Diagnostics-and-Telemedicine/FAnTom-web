package repository

import model.CTType
import model.Filter
import model.Research
import model.ResearchSlicesSizesData

interface ResearchRepository {

  val token: suspend () -> String

  suspend fun getResearches(): List<Research>
  suspend fun getFiltered(filter: Filter): List<Research>
  suspend fun initResearch(researchId: Int): ResearchSlicesSizesData
  suspend fun getSlice(
    researchId: Int,
    black: Int,
    white: Int,
    gamma: Double,
    type: Int,
    mipMethod: Int,
    sliceNumber: Int,
    aproxSize: Int
  ): String

  suspend fun getHounsfieldData(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): Double
  suspend fun confirmCtTypeForResearch(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  )

  suspend fun closeSession(researchId: Int)
}
