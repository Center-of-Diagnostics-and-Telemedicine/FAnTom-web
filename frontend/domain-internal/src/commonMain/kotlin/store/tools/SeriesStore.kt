package store.tools

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import model.init.SeriesModel
import store.tools.SeriesStore.*

interface SeriesStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class ChangeSeries(val seriesName: String) : Intent()
  }

  data class State(
    val series: Map<String, SeriesModel> = mapOf(),
    val currentSeries: SeriesModel? = null
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
  }
}