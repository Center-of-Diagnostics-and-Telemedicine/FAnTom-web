package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.Mip

class MyMipRepositoryImpl : MyMipRepository {

  private val _mipSubject = BehaviorSubject<Mip>(Mip.initial)
  override val mipMethod: Observable<Mip> = _mipSubject

  override fun setMip(mip: Mip) {
    _mipSubject.onNext(mip)
  }
}