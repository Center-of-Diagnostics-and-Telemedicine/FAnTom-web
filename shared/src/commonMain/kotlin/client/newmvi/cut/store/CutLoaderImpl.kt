package client.newmvi.cut.store

import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturnValue
import com.badoo.reaktive.single.subscribeOn
import client.domain.repository.ResearchRepository
import model.SliceData

class CutLoaderImpl(
  private val repository: ResearchRepository
) : CutLoader {

  override fun load(sliceData: SliceData): Single<CutLoader.Result> =
    singleFromCoroutine {
      repository.getSlice(
        black = sliceData.black,
        white = sliceData.white,
        gamma = sliceData.gamma,
        researchId = sliceData.researchId,
        type = sliceData.cutType,
        mipMethod = sliceData.mipMethod,
        slyceNumber = sliceData.sliceNumber,
        aproxSize = sliceData.mipValue
      )
    }
      .map(CutLoader.Result::Success)
      .subscribeOn(ioScheduler)
      .onErrorReturnValue(CutLoader.Result.Error("Произошла ошибка"))
}