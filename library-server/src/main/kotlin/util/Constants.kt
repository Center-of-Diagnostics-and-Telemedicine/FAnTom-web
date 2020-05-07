package util

class NotInitializedYetException() : Exception()
class NotInitializedException(): Exception()

sealed class LibraryState {

  object ReadyToInitLib: LibraryState()
  object InitLibProcess: LibraryState()

  object ReadyToInitResearch: LibraryState()
  object InitResearchProcess: LibraryState()
  object ResearchInitialized: LibraryState()
}