package repository

import com.badoo.reaktive.observable.Observable
import model.Mip

interface MyMipRepository {

  val mipMethod: Observable<Mip>

  fun setMip(mip: Mip)
}