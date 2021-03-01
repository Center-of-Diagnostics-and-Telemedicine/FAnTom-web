import dao.*
import kotlinx.coroutines.GlobalScope
import repository.*
import repository.repository.ExportedMarksRepository

val userDao = UserDao()
val userRepository = UserRepositoryImpl(userDao)

val researchDao = ResearchDao()
val researchRepository = ResearchRepositoryImpl(researchDao)

val userResearchDao = UserResearchDao()
val userResearchRepository = UserResearchRepositoryImpl(userResearchDao)

val exportedMarksDao = ExportedMarksDao()
val exportedMarksRepository = ExportedMarksRepositoryImpl(exportedMarksDao)