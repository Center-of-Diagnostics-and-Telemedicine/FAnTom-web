package client.newmvi.cut.store

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import client.domain.repository.ResearchRepository
import com.badoo.reaktive.single.*
import model.AreaToSave

class AreaSaverImpl(
  private val repository: ResearchRepository
) : AreaSaver {

  override fun save(
    selectedAreaWithoutId: AreaToSave,
    researchId: Int
  ): Single<AreaSaver.Result> {
    return singleFromCoroutine {
      repository.createMark(researchId, selectedAreaWithoutId)
    }
      .map(AreaSaver.Result::Success)
      .subscribeOn(ioScheduler)
      .onErrorReturnValue(AreaSaver.Result.Error("Произошла ошибка при сохранении сегментации"))
  }

}