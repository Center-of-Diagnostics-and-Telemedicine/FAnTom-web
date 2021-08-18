package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.EmptyGridModel
import model.MyNewGrid

class GridRepositoryImpl : GridRepository {

  private val _gridSubject = BehaviorSubject<MyNewGrid>(EmptyGridModel)
  override val grid: Observable<MyNewGrid> = _gridSubject

  override fun changeGrid(grid: MyNewGrid) {
    _gridSubject.onNext(grid)
  }

}