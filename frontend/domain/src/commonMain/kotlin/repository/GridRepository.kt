package repository

import com.badoo.reaktive.observable.Observable
import model.GridModel
import model.GridType
import model.Plane

interface GridRepository {

  val grid: Observable<GridType>

  fun changeGrid(gridType: GridType)

}