package client.newmvi.researchmvi.store

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturnValue
import client.domain.repository.ResearchRepository

class ResearchDataLoaderImpl(
  private val repository: ResearchRepository
) : ResearchDataLoader {

  override fun load(researchId: Int): Single<ResearchDataLoader.Result> =
    singleFromCoroutine {
      repository.initResearch(researchId)
    }
      .map {
        val result = it.copy(researchId = researchId)
        result
      }
      .map(ResearchDataLoader.Result::Success)
      .onErrorReturnValue(ResearchDataLoader.Result.Error("Произошла ошибка"))
}