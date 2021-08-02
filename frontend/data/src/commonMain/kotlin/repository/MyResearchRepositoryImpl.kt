package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.*

class MyResearchRepositoryImpl(
  val local: ResearchLocal,
  val remote: ResearchRemote,
  override val token: suspend () -> String,
) : MyResearchRepository {

  private val _research: Subject<Research?> = BehaviorSubject(null)
  override val research: Observable<Research?> = _research

  private val _researches: Subject<List<Research>?> = BehaviorSubject(null)
  override val researches: Observable<List<Research>?> = _researches

  private val _researchData: Subject<ResearchDataModel?> = BehaviorSubject(null)
  override val researchData: Observable<ResearchDataModel?> = _researchData

  override suspend fun loadResearches() {
    val response = remote.getAll(token())
    when {
      response.response != null -> {
        val researches = response.response!!.researches
        _researches.onNext(researches)
        local.saveList(researches)
      }
      response.error != null -> mapErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchListFetchException
    }
  }

  override suspend fun applyFilter(filter: Filter, category: Category) {
    val cached = local.getAll()
    val filteredByVisibility = when (filter) {
      Filter.All -> cached
      Filter.NotSeen -> cached.filterNot { it.seen }
      Filter.Seen -> cached.filter { it.seen }
      Filter.Done -> cached.filter { it.done }
    }

    val filtered = when (category) {
      Category.All -> filteredByVisibility
      else -> filteredByVisibility.filter { it.category == category.name }
    }

    _researches.onNext(filtered)
  }

  override suspend fun initResearch(researchId: Int) {
    val response = remote.init(token(), researchId)
    val research = local.get(researchId)

    when {
      response.response != null -> {
        val researchData = response.response!!.toResearchData(researchId)
        _researchData.onNext(researchData)
        _research.onNext(research)
      }
      response.error != null -> mapErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.ResearchInitializationException
    }
  }

  override suspend fun closeSession(researchId: Int) {
    val response = remote.closeSession(token(), researchId)
    when {
      response.response != null -> return
      response.error != null -> mapErrorResponse<Boolean>(response.error!!)
      else -> throw ResearchApiExceptions.CloseResearchException
    }
  }

  override suspend fun closeResearch(researchId: Int) {
    val response = remote.closeResearch(token(), researchId)
    when {
      response.response != null -> return
      response.error != null -> mapErrorResponse<Boolean>(response.error!!)
      else -> throw ResearchApiExceptions.CloseSessionException
    }
  }

  override suspend fun getSlice(model: GetSliceModel): String {
    val response = remote.getSlice(
      token = token(),
      request = model.toSliceRequest(),
      researchId = model.researchId
    )
    return when {
      response.response != null -> response.response!!.image.removeSuffix("\\u003d")
      response.error != null -> mapErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.SliceFetchException
    }
  }

  private fun <T : Any> mapErrorResponse(response: ErrorModel): T {
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