package repository

import com.badoo.reaktive.observable.Observable
import model.Research
import model.ResearchDataModel

interface MyResearchRepository: ResearchRepository {
  val research: Observable<Research>
  val researches: Observable<List<Research>>
  val researchData: Observable<ResearchDataModel>
}