package repository

import com.badoo.reaktive.observable.Observable
import model.MyNewGrid

interface GridRepository {

  val grid: Observable<MyNewGrid>

  fun changeGrid(grid: MyNewGrid)

}