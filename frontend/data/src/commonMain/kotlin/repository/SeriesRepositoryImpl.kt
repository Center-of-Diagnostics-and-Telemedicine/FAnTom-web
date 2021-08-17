package repository

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import model.SeriesModel

class SeriesRepositoryImpl : SeriesRepository {

  private val _currentSeriesSubject = BehaviorSubject<SeriesModel?>(null)
  override val currentSeries: Observable<SeriesModel> = _currentSeriesSubject.mapNotNull { it }

  private val _seriesSubject = BehaviorSubject<Map<String, SeriesModel>>(mapOf())
  override val series: Observable<Map<String, SeriesModel>> = _seriesSubject

  override fun changeSeries(seriesModel: SeriesModel) {
    _currentSeriesSubject.onNext(seriesModel)
  }

  override fun seriesLoaded(map: Map<String, SeriesModel>) {
    _seriesSubject.onNext(map)
  }
}