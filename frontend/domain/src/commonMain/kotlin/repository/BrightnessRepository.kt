package repository

import com.badoo.reaktive.utils.atomic.AtomicInt
import com.badoo.reaktive.utils.atomic.AtomicReference
import model.INITIAL_BLACK
import model.INITIAL_GAMMA
import model.INITIAL_WHITE

interface BrightnessRepository {

  fun getBlackValue(): Int
  fun setBlackValue(value: Int)

  fun getWhiteValue(): Int
  fun setWhiteValue(value: Int)

  fun getGammaValue(): Double
  fun setGammaValue(value: Double)

}

class BrightnessRepositoryImpl : BrightnessRepository {

  private val black: AtomicInt = AtomicInt(INITIAL_BLACK.toInt())
  private val white: AtomicInt = AtomicInt(INITIAL_WHITE.toInt())
  private val gamma: AtomicReference<Double> = AtomicReference(INITIAL_GAMMA)

  override fun getBlackValue(): Int {
    return black.value
  }

  override fun setBlackValue(value: Int) {
    black.compareAndSet(black.value, value)
  }

  override fun getWhiteValue(): Int {
    return white.value
  }

  override fun setWhiteValue(value: Int) {
    white.compareAndSet(white.value, value)
  }

  override fun getGammaValue(): Double {
    return gamma.value
  }

  override fun setGammaValue(value: Double) {
    gamma.compareAndSet(gamma.value, value)
  }


}