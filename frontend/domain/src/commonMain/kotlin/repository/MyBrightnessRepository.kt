package repository

import com.badoo.reaktive.observable.Observable

interface MyBrightnessRepository {

  val black: Observable<Int>
  val white: Observable<Int>
  val gamma: Observable<Double>

  fun setBlack(black: Int)
  fun setWhite(white: Int)
  fun setGamma(gamma: Double)

}