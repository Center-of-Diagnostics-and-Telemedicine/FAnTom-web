package client.newmvi.cut.store

import com.badoo.reaktive.completable.asSingle
import com.badoo.reaktive.coroutinesinterop.completableFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.onErrorReturnValue
import com.badoo.reaktive.single.subscribeOn
import client.domain.repository.ResearchRepository

class AreaDeleterImpl(
  private val repository: ResearchRepository
) : AreaDeleter {

  override fun delete(
    areaId: Int
  ): Single<AreaDeleter.Result> {
    return completableFromCoroutine {
      repository.deleteMark(areaId)
    }
      .asSingle(AreaDeleter.Result.Success(areaId))
      .subscribeOn(ioScheduler)
      .onErrorReturnValue(AreaDeleter.Result.Error("Произошла ошибка при сохранении сегментации"))
  }

}