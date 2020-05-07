package client.newmvi.cut.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface CutView : BaseView<CutView.Event> {

  val events: PublishSubject<Event>

  fun show(model: CutViewModel)

  data class CutViewModel(
    val isLoading: Boolean,
    val error: String,
    val url: ByteArray = byteArrayOf()
  ) {
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || this::class != other::class) return false

      other as CutViewModel

      if (isLoading != other.isLoading) return false
      if (error != other.error) return false
      if (!url.contentEquals(other.url)) return false

      return true
    }

    override fun hashCode(): Int {
      var result = isLoading.hashCode()
      result = 31 * result + error.hashCode()
      result = 31 * result + url.contentHashCode()
      return result
    }
  }

  sealed class Event : BaseEvent {
    object ErrorShown : Event()
  }
}