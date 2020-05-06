import dao.*
import repository.*

val userDao = UserDao()
val userRepository = UserRepositoryImpl(userDao)

val researchDao = ResearchDao()
val researchRepository = ResearchRepositoryImpl(researchDao)

val userResearchDao = UserResearchDao()
val userResearchRepository = UserResearchRepositoryImpl(userResearchDao)

val marksDao = MarkDao()
val marksRepository = MarkRepositoryImpl(marksDao)

val creator = LibraryCreatorImpl()
val researchDirFinder = ResearchDirFinderImpl()
val sessionRepository = SessionRepositoryImpl(creator, researchDirFinder)