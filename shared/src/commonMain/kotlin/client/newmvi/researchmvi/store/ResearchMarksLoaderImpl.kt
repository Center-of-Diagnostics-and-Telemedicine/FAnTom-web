package client.newmvi.researchmvi.store

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturnValue
import client.domain.repository.ResearchRepository

class ResearchMarksLoaderImpl(
  private val repository: ResearchRepository
) : ResearchMarksLoader {

  override fun load(researchId: Int): Single<ResearchMarksLoader.Result> =
    singleFromCoroutine {
      repository.getMarks(researchId)
    }
      .map(ResearchMarksLoader.Result::Success)
      .onErrorReturnValue(ResearchMarksLoader.Result.Error("Произошла ошибка"))
}