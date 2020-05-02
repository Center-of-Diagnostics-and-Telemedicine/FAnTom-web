package client.newmvi.cut.store

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import client.domain.repository.ResearchRepository
import com.badoo.reaktive.single.*

import model.SelectedArea

class AreaUpdaterImpl(
  private val repository: ResearchRepository
) : AreaUpdater {

  override fun update(
    selectedArea: SelectedArea
  ): Single<AreaUpdater.Result> {
    return singleFromCoroutine {
      repository.updateMark(selectedArea)
    }
      .map(AreaUpdater.Result::Success)
      .subscribeOn(ioScheduler)
      .onErrorReturnValue(AreaUpdater.Result.Error("Произошла ошибка при сохранении сегментации"))
  }

}