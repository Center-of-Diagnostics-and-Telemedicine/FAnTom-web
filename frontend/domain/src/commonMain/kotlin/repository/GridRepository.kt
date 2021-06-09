package repository

import com.badoo.reaktive.observable.Observable
import model.GridType

interface GridRepository {

  val grid: Observable<GridType>

  fun changeGrid(gridType: GridType)

}