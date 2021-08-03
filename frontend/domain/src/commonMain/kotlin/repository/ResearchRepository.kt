package repository

import model.*

interface ResearchRepository {

  val token: suspend () -> String

  suspend fun getResearches(): List<Research>
  suspend fun getFiltered(filter: Filter, category: Category): List<Research>
  suspend fun initResearch(researchId: Int, doseReport: Boolean): ResearchData
  suspend fun getSlice(
    researchId: Int,
    black: Int,
    white: Int,
    gamma: Double,
    type: Int,
    mipMethod: Int,
    sliceNumber: Int,
    aproxSize: Int,
    width: Int,
    height: Int
  ): String

  suspend fun getSlice(model: GetSliceModel): String

  suspend fun getHounsfieldData(
    sliceNumber: Int,
    type: Int,
    mipMethod: Int,
    mipValue: Int,
    horizontal: Int,
    vertical: Int,
    width: Int,
    height: Int
  ): Double

  suspend fun confirmCtTypeForResearch(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  )

  suspend fun closeSession(researchId: Int)
  suspend fun closeResearch(researchId: Int)
}

data class GetSliceModel(
  val researchId: Int,
  val black: Int,
  val white: Int,
  val gamma: Double,
  val type: Int,
  val mipMethod: Int,
  val sliceNumber: Int,
  val aproxSize: Int,
  val width: Int,
  val height: Int,
  val sopInstanceUid: String
)
