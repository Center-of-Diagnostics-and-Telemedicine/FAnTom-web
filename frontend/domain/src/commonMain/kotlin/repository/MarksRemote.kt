package repository

import model.*

interface MarksRemote {
  suspend fun getAll(token: String, researchId: Int): MarksResponseNew
  suspend fun save(request: MarkData, researchId: Int, token: String): MarkResponse
  suspend fun delete(markId: Int, researchId: Int, token: String): BaseResponse
  suspend fun update(request: MarkEntity, researchId: Int, token: String): BaseResponse
}
