package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.*
import replace

class MyMarksRepositoryImpl(
  val remote: MarksRemote,
  override val token: suspend () -> String
) : MyMarksRepository {

  private val _mark: BehaviorSubject<MarkEntity?> = BehaviorSubject(null)
  override val mark: Observable<MarkEntity?> = _mark

  private val _marks: BehaviorSubject<List<MarkEntity>?> = BehaviorSubject(null)
  override val marks: Observable<List<MarkEntity>?> = _marks

  override suspend fun setMark(mark: MarkEntity?) {
    _mark.onNext(mark)
  }

  override suspend fun loadMarks(researchId: Int) {
    val response = remote.getAll(token(), researchId)
    when {
      response.response != null -> {
        val marks = response.response!!.list
        _marks.onNext(marks)
      }
      response.error != null -> mapErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.MarksFetchException
    }
  }

  override suspend fun saveMark(markToSave: MarkData, researchId: Int) {
    val response = remote.save(markToSave, researchId, token())
    when {
      response.response != null -> {
        val mark = response.response!!.mark
        val marks = _marks.value?.plus(mark) ?: listOf(mark)
        _marks.onNext(marks)
        setMark(mark)
      }
      response.error != null -> mapErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.MarkCreateException
    }
  }

  override suspend fun updateMark(mark: MarkEntity, researchId: Int) {
    val response = remote.update(mark, researchId, token())
    when {
      response.response != null -> {
        val marks = _marks.value?.replace(mark) { it.id == mark.id } ?: listOf(mark)
        _marks.onNext(marks)
      }
      response.error != null -> mapErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.MarkUpdateException
    }
  }

  override suspend fun deleteMark(id: Int, researchId: Int) {
    val response = remote.delete(id, researchId, token())
    when {
      response.response != null -> {
        val marks = _marks.value?.filter { it.id != id }
        _marks.onNext(marks)
      }
      response.error != null -> mapErrorResponse(response.error!!)
      else -> throw ResearchApiExceptions.MarkDeleteException
    }
  }

  private fun <T : Any> mapErrorResponse(response: ErrorModel): T {
    when (response.error) {
      ErrorStringCode.RESEARCH_NOT_FOUND.value -> throw ResearchApiExceptions.ResearchNotFoundException

      ErrorStringCode.GET_MARKS_FAILED.value -> throw ResearchApiExceptions.MarksFetchException
      ErrorStringCode.CREATE_MARK_FAILED.value -> throw ResearchApiExceptions.MarkCreateException
      ErrorStringCode.UPDATE_MARK_FAILED.value -> throw ResearchApiExceptions.MarkUpdateException

      ErrorStringCode.SESSION_CLOSE_FAILED.value -> throw ResearchApiExceptions.CloseSessionException
      ErrorStringCode.SESSION_EXPIRED.value -> throw ResearchApiExceptions.SessionExpiredException
      else -> throw Exception(BASE_ERROR)
    }
  }

}