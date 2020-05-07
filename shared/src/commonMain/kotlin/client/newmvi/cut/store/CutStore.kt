package client.newmvi.cut.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.SliceData

interface CutStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val url: ByteArray = byteArrayOf(),
    val sliceData: SliceData
  ) {
    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null || this::class != other::class) return false

      other as State

      if (isLoading != other.isLoading) return false
      if (error != other.error) return false
      if (!url.contentEquals(other.url)) return false
      if (sliceData != other.sliceData) return false

      return true
    }

    override fun hashCode(): Int {
      var result = isLoading.hashCode()
      result = 31 * result + error.hashCode()
      result = 31 * result + url.contentHashCode()
      result = 31 * result + sliceData.hashCode()
      return result
    }
  }

  sealed class Intent {
    class GetUrl(val sliceData: SliceData) : Intent()

    object DismissError : Intent()
  }
}