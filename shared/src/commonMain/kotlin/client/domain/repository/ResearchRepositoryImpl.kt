package client.domain.repository

import client.datasource.local.LocalDataSource
import client.datasource.remote.ResearchRemote
import model.*
import model.ResearchApiExceptions.*

interface ResearchRepository : Repository {

  suspend fun getResearches(): List<Research>
  suspend fun initResearch(researchId: Int): ResearchSlicesSizesData
  suspend fun getSlice(
    researchId: Int,
    black: Double,
    white: Double,
    gamma: Double,
    type: Int,
    mipMethod: Int,
    slyceNumber: Int,
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

class ResearchRepositoryImpl(
  private val local: LocalDataSource,
  private val remote: ResearchRemote
) : ResearchRepository {

  override suspend fun getResearches(): List<Research> {
    val response = remote.getResearches(local.getToken())
    return when {
      response.response != null -> response.response!!.researches
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchListFetchException
    }
  }

  override suspend fun initResearch(researchId: Int): ResearchSlicesSizesData {
    val response = remote.initResearch(
      token = local.getToken(),
      researchId = researchId
    )
    return when {
      response.response != null -> response.response!!.toResearchSlicesSizesData()
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchInitializationException
    }
  }

  override suspend fun getSlice(
    researchId: Int,
    black: Double,
    white: Double,
    gamma: Double,
    type: Int,
    mipMethod: Int,
    slyceNumber: Int,
    aproxSize: Int
  ): String {
    val response = remote.getSlice(
      token = local.getToken(),
      request = SliceRequest(
        black = black.toInt(),
        white = white.toInt(),
        gamma = gamma,
        sliceType = type,
        mipMethod = mipMethod,
        sliceNumber = slyceNumber,
        mipValue = aproxSize
      ),
      researchId = researchId
    )
    return when {
      response.response != null -> response.response!!.image.removeSuffix("\\u003d")
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw SliceFetchException
    }
  }

  override suspend fun getHounsfieldData(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Double {
    val response = remote.getHounsfieldData(
      local.getToken(),
      HounsfieldRequest(axialCoord, frontalCoord, sagittalCoord)
    )
    return when {
      response.response != null -> response.response!!.brightness ?: 0.0
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw HounsfieldFetchError
    }
  }

  override suspend fun confirmCtTypeForResearch(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  ) {
    val response = remote.confirmCtTypeForResearch(
      local.getToken(),
      ConfirmCTTypeRequest(researchId, ctType.ordinal, leftPercent, rightPercent)
    )
    when {
      response.response != null -> return
      response.error != null -> handleErrorResponse<Boolean>(response.error!!)
      else -> throw ConfirmCtTypeForResearchException
    }
  }

  override suspend fun closeSession(researchId: Int) {
    val response = remote.closeSession(local.getToken(), researchId)
    when {
      response.response != null -> return
      response.error != null -> handleErrorResponse<Boolean>(response.error!!)
      else -> throw CloseSessionException
    }
  }

  private fun <T : Any> handleErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.USER_RESEARCHES_LIST_FAILED.value -> throw ResearchListFetchException

      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchNotFoundException
      ErrorStringCode.RESEARCH_INITIALIZATION_FAILED.value -> throw ResearchInitializationException

      ErrorStringCode.HOUNSFIELD_ERROR.value -> throw HounsfieldFetchError
      ErrorStringCode.INCORRECT_AXIAL_COORD.value -> throw IncorrectAxialValueException
      ErrorStringCode.INCORRECT_FRONTAL_COORD.value -> throw IncorrectFrontalValueException
      ErrorStringCode.INCORRECT_SAGITTAL_COORD.value -> throw IncorrectSagittalValueException

      ErrorStringCode.INCORRECT_SLICE_NUMBER.value -> throw IncorrectSliceNumberException
      ErrorStringCode.GET_SLICE_FAILED.value -> throw SliceFetchException

      ErrorStringCode.CREATE_MARK_FAILED.value -> throw ConfirmCtTypeForResearchException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }
}
