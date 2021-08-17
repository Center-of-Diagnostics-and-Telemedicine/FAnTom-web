package repository

import com.badoo.reaktive.observable.Observable
import model.SeriesModel

interface SeriesRepository {

  val currentSeries: Observable<SeriesModel>
  val series: Observable<Map<String, SeriesModel>>

  fun changeSeries(seriesModel: SeriesModel)
  fun seriesLoaded(map: Map<String, SeriesModel>)

}