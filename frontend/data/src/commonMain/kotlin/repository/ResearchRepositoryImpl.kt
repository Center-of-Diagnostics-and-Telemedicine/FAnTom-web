package repository

import model.*
import model.ResearchApiExceptions.*

class ResearchRepositoryImpl(
  val local: ResearchLocal,
  val remote: ResearchRemote,
  override val token: suspend () -> String
) : ResearchRepository {

  override suspend fun getResearches(): List<Research> {
    val response = remote.getAll(token())
    return when {
      response.response != null -> {
        val researches = response.response!!.researches
        local.saveList(researches)
        researches
      }
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchListFetchException
    }
  }

  override suspend fun getFiltered(filter: Filter, category: Category): List<Research> {
    val cached = local.getAll()
    val filteredByVisibility = when (filter) {
      Filter.All -> cached
      Filter.NotSeen -> cached.filterNot { it.seen }
      Filter.Seen -> cached.filter { it.seen }
      Filter.Done -> cached.filter { it.done }
    }

    return when (category) {
      Category.All -> filteredByVisibility
      else -> filteredByVisibility.filter { it.category == category.name }
    }
  }

  override suspend fun initResearch(researchId: Int, doseReport: Boolean): ResearchData {
    val response = remote.init(
      token = token(),
      id = researchId
    )

    return when {
      response.response != null -> response.response!!.toResearchSlicesSizesData(doseReport)
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchInitializationException
    }
  }

  override suspend fun getSlice(
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
  ): String {
    val response = remote.getSlice(
      token = token(),
      request = SliceRequestNew(
        image = ImageModel(
          modality = getModalityStringType(type),
          type = getSliceStringType(type),
          number = sliceNumber,
          mip = MipModel(
            mip_method = getMipMethodStringType(mipMethod),
            mip_value = aproxSize
          ),
          width = width,
          height = height
        ),
        brightness = BrightnessModel(
          black = black,
          white = white,
          gamma = gamma
        )
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
    sliceNumber: Int,
    type: Int,
    mipMethod: Int,
    mipValue: Int,
    horizontal: Int,
    vertical: Int,
    width: Int,
    height: Int
  ): Double {
    val response = remote.hounsfield(
      token = token(),
      request = HounsfieldRequestNew(
        image = ImageModel(
          modality = getModalityStringType(type), //TODO(remove this),
          type = getSliceStringType(type),
          number = sliceNumber,
          mip = MipModel(
            mip_method = getMipMethodStringType(mipMethod),
            mip_value = mipValue
          ),
          height = height,
          width = width
        ),
        point = PointModel(
          vertical = vertical,
          horizontal = horizontal
        )
      )
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
      token(),
      ConfirmCTTypeRequest(researchId, ctType.ordinal, leftPercent, rightPercent)
    )
    when {
      response.response != null -> return
      response.error != null -> handleErrorResponse<Boolean>(response.error!!)
      else -> throw ConfirmCtTypeForResearchException
    }
  }

  override suspend fun closeSession(researchId: Int) {
    val response = remote.closeSession(token(), researchId)
    when {
      response.response != null -> return
      response.error != null -> handleErrorResponse<Boolean>(response.error!!)
      else -> throw CloseResearchException
    }
  }

  override suspend fun closeResearch(researchId: Int) {
    val response = remote.closeResearch(token(), researchId)
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
      ErrorStringCode.RESEARCH_DATA_FETCH_FAILED.value -> throw ResearchDataFetchError
      ErrorStringCode.RESEARCH_CLOSE_FAILED.value -> throw CloseResearchException

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
