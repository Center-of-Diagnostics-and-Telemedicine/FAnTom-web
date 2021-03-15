import dao.ExportedMarksDao
import dao.ResearchDao
import dao.UserDao
import dao.UserResearchDao
import repository.ExportedMarksRepositoryImpl
import repository.ResearchRepositoryImpl
import repository.UserRepositoryImpl
import repository.UserResearchRepositoryImpl

val userDao = UserDao()
val userRepository = UserRepositoryImpl(userDao)

val researchDao = ResearchDao()
val researchRepository = ResearchRepositoryImpl(researchDao)

val userResearchDao = UserResearchDao()
val userResearchRepository = UserResearchRepositoryImpl(userResearchDao)

val exportedMarksDao = ExportedMarksDao()
val exportedMarksRepository = ExportedMarksRepositoryImpl(exportedMarksDao)