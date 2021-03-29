import dao.*
import repository.*

val userDao = UserDao()
val userRepository = UserRepositoryImpl(userDao)

val researchDao = ResearchDao()
val researchRepository = ResearchRepositoryImpl(researchDao)

val userResearchDao = UserResearchDao()
val userResearchRepository = UserResearchRepositoryImpl(userResearchDao)

val exportedMarksDao = ExportedMarksDao()
val exportedMarksRepository = ExportedMarksRepositoryImpl(exportedMarksDao)


val planarMarksDao = PlanarMarksDao()
val planarMarksRepository = PlanarMarksRepositoryImpl(planarMarksDao)

val multiPlanarMarksDao = MultiPlanarMarksDao()
val multiPlanarMarksRepository = MultiPlanarMarksRepositoryImpl(multiPlanarMarksDao)