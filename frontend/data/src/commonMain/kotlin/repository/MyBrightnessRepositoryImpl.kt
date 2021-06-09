package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.INITIAL_BLACK
import model.INITIAL_GAMMA
import model.INITIAL_WHITE

class MyBrightnessRepositoryImpl : MyBrightnessRepository {

  private val _blackSubject = BehaviorSubject(INITIAL_BLACK.toInt())
  private val _whiteSubject = BehaviorSubject(INITIAL_WHITE.toInt())
  private val _gammaSubject = BehaviorSubject(INITIAL_GAMMA)

  override val black: Observable<Int> = _blackSubject
  override val white: Observable<Int> = _whiteSubject
  override val gamma: Observable<Double> = _gammaSubject

  override fun setBlack(black: Int) {
    _blackSubject.onNext(black)
  }

  override fun setWhite(white: Int) {
    _whiteSubject.onNext(white)
  }

  override fun setGamma(gamma: Double) {
    _gammaSubject.onNext(gamma)
  }
}