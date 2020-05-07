package client.domain.repository

import client.ResearchApiExceptions.*
import client.datasource.local.LocalDataSource
import client.datasource.remote.ResearchRemote
import model.*

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
  ): ByteArray
  suspend fun getHounsfieldData(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): Double
  suspend fun confirmCtTypeForResearch(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  )

  suspend fun closeSession()
}

class ResearchRepositoryImpl(
  private val local: LocalDataSource,
  private val remote: ResearchRemote
) : ResearchRepository {

  override suspend fun getResearches(): List<Research> {
    return when (val response = remote.getResearches(local.getToken())) {
      is ApiResponse.ResearchesResponse -> response.researches
      is ApiResponse.ErrorResponse -> handleErrorResponse(response)
      else -> throw ResearchListFetchException
    }
  }

  override suspend fun initResearch(researchId: Int): ResearchSlicesSizesData {
    val response = remote.initResearch(
      token = local.getToken(),
      researchId = researchId
    )
    return when (response) {
      is ApiResponse.ResearchInitResponse -> response.toResearchSlicesSizesData()
      is ApiResponse.ErrorResponse -> handleErrorResponse(response)
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
  ): ByteArray {
    val response = remote.getSlice(
      token = local.getToken(),
      request = SliceRequest(
        black = black,
        white = white,
        gamma = gamma,
        type = type,
        mipMethod = mipMethod,
        sliceNumber = slyceNumber,
        mipValue = aproxSize
      ),
      researchId = researchId
    )
    return when (response) {
      is ApiResponse.SliceResponse -> response.image
      is ApiResponse.ErrorResponse -> handleErrorResponse(response)
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
    return when (response) {
      is ApiResponse.HounsfieldResponse -> response.huValue
      is ApiResponse.ErrorResponse -> handleErrorResponse(response)
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
    when (response) {
      is ApiResponse.OK -> return
      is ApiResponse.ErrorResponse -> handleErrorResponse<Boolean>(response)
      else -> throw ConfirmCtTypeForResearchException
    }
  }

  override suspend fun closeSession() {
    when (val response = remote.closeSession(local.getToken())) {
      is ApiResponse.OK -> return
      is ApiResponse.ErrorResponse -> handleErrorResponse<Boolean>(response)
      else -> throw CloseSessionException
    }
  }

  private fun <T : Any> handleErrorResponse(response: ApiResponse.ErrorResponse): T {
    when (response.errorCode) {
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