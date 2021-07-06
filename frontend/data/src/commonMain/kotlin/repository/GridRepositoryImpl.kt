package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.*

class GridRepositoryImpl : GridRepository {

  private val _gridSubject = BehaviorSubject<GridType>(GridType.initial)
  override val grid: Observable<GridType> = _gridSubject

  override fun changeGrid(gridType: GridType) {
    _gridSubject.onNext(gridType)
  }

}