package util

class NotInitializedYetException(val myMessage: String = "") : Exception()
class NotInitializedException(val myMessage: String = ""): Exception()

sealed class LibraryState {

  object ReadyToInitLib: LibraryState()
  object InitLibProcess: LibraryState()

  object ReadyToInitResearch: LibraryState()
  object InitResearchProcess: LibraryState()
  object ResearchInitialized: LibraryState()
}