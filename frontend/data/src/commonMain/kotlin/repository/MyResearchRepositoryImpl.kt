package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.*

class MyResearchRepositoryImpl(
  val local: ResearchLocal,
  val remote: ResearchRemote,
  override val token: suspend () -> String
) : MyResearchRepository {

  private val initialResearch = Research(
    id = -1,
    name = "",
    seen = false,
    done = false,
    marked = false,
    modality = "",
    category = "",
  )
  private val initialResearchData = ResearchDataModel(
    planes = mapOf(),
    type = ResearchType.CT,
    reversed = false,
    markTypes = mapOf(),
  )
  private val initialResearches = listOf<Research>()

  private val _research = BehaviorSubject(initialResearch)
  override val research: Observable<Research> = _research

  private val _researches = BehaviorSubject(initialResearches)
  override val researches: Observable<List<Research>> = _researches

  private val _researchData = BehaviorSubject(initialResearchData)
  override val researchData: Observable<ResearchDataModel> = _researchData

  override suspend fun getResearches(): List<Research> {
    val response = remote.getAll(token())
    return when {
      response.response != null -> {
        val researches = response.response!!.researches
        _researches.onNext(researches)
        local.saveList(researches)
        researches
      }
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchListFetchException
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

  override suspend fun initResearch(researchId: Int, doseReport: Boolean): ResearchDataModel {
    val response = remote.init(token(), researchId)
    val research = local.get(researchId)

    return when {
      response.response != null -> {
        val researchData = response.response!!.toResearchData(researchId)
        _researchData.onNext(researchData)
        _research.onNext(research)
        researchData
      }
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchInitializationException
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
          sop_instance_uid = "",
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
      else -> throw ResearchApiExceptions.SliceFetchException
    }
  }

  override suspend fun getSlice(model: GetSliceModel): String {
    val response = remote.getSlice(
      token = token(),
      request = SliceRequestNew(
        image = ImageModel(
          modality = getModalityStringType(model.type),
          type = getSliceStringType(model.type),
          number = model.sliceNumber,
          sop_instance_uid = model.sopInstanceUid,
          mip = MipModel(
            mip_method = getMipMethodStringType(model.mipMethod),
            mip_value = model.aproxSize
          ),
          width = model.width,
          height = model.height
        ),
        brightness = BrightnessModel(
          black = model.black,
          white = model.white,
          gamma = model.gamma
        )
      ),
      researchId = model.researchId
    )
    return when {
      response.response != null -> response.response!!.image.removeSuffix("\\u003d")
      response.error != null -> handleErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.SliceFetchException
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
          sop_instance_uid = "",
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
      else -> throw ResearchApiExceptions.HounsfieldFetchError
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
      else -> throw ResearchApiExceptions.ConfirmCtTypeForResearchException
    }
  }

  override suspend fun closeSession(researchId: Int) {
    val response = remote.closeSession(token(), researchId)
    when {
      response.response != null -> return
      response.error != null -> handleErrorResponse<Boolean>(response.error!!)
      else -> throw ResearchApiExceptions.CloseResearchException
    }
  }

  override suspend fun closeResearch(researchId: Int) {
    val response = remote.closeResearch(token(), researchId)
    when {
      response.response != null -> return
      response.error != null -> handleErrorResponse<Boolean>(response.error!!)
      else -> throw ResearchApiExceptions.CloseSessionException
    }
  }

  private fun <T : Any> handleErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.USER_RESEARCHES_LIST_FAILED.value -> throw ResearchApiExceptions.ResearchListFetchException

      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchApiExceptions.ResearchNotFoundException
      ErrorStringCode.RESEARCH_INITIALIZATION_FAILED.value -> throw ResearchApiExceptions.ResearchInitializationException
      ErrorStringCode.RESEARCH_DATA_FETCH_FAILED.value -> throw ResearchApiExceptions.ResearchDataFetchError
      ErrorStringCode.RESEARCH_CLOSE_FAILED.value -> throw ResearchApiExceptions.CloseResearchException

      ErrorStringCode.HOUNSFIELD_ERROR.value -> throw ResearchApiExceptions.HounsfieldFetchError
      ErrorStringCode.INCORRECT_AXIAL_COORD.value -> throw ResearchApiExceptions.IncorrectAxialValueException
      ErrorStringCode.INCORRECT_FRONTAL_COORD.value -> throw ResearchApiExceptions.IncorrectFrontalValueException
      ErrorStringCode.INCORRECT_SAGITTAL_COORD.value -> throw ResearchApiExceptions.IncorrectSagittalValueException

      ErrorStringCode.INCORRECT_SLICE_NUMBER.value -> throw ResearchApiExceptions.IncorrectSliceNumberException
      ErrorStringCode.GET_SLICE_FAILED.value -> throw ResearchApiExceptions.SliceFetchException

      ErrorStringCode.CREATE_MARK_FAILED.value -> throw ResearchApiExceptions.ConfirmCtTypeForResearchException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw ResearchApiExceptions.CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw ResearchApiExceptions.SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }
}