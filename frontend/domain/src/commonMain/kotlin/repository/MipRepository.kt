package repository

import com.badoo.reaktive.utils.atomic.AtomicInt
import com.badoo.reaktive.utils.atomic.AtomicReference
import model.INITIAL_MIP_VALUE
import model.Mip

interface MipRepository {

  fun getMip(): Mip
  fun setMip(value: Mip)

  fun getMipValue(): Int
  fun setMipValue(value: Int)

}

class MipRepositoryImpl : MipRepository {

  val mip: AtomicReference<Mip> = AtomicReference(Mip.No)
  val mipValue: AtomicInt = AtomicInt(INITIAL_MIP_VALUE)

  override fun getMip(): Mip {
    return mip.value
  }

  override fun setMip(value: Mip) {
    mip.compareAndSet(mip.value, value)
  }

  override fun getMipValue(): Int {
    return mipValue.value
  }

  override fun setMipValue(value: Int) {
    mipValue.compareAndSet(mipValue.value, value)
  }

}