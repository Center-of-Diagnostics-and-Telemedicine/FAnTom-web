import dao.*
import repository.*

val userDao = UserDao()
val userRepository = UserRepositoryImpl(userDao)

val researchDao = ResearchDao()
val researchRepository = ResearchRepositoryImpl(researchDao)

val userResearchDao = UserResearchDao()
val userResearchRepository = UserResearchRepositoryImpl(userResearchDao)

val exportedMarksDao = ExpertMarksDao()
val exportedMarksRepository = ExpertMarksRepositoryImpl(exportedMarksDao)

val exportedRoisDao = ExportedRoisDao()
val exportedRoisRepository = ExportedRoisRepositoryImpl(exportedRoisDao)


val planarMarksDao = PlanarMarksDao()
val planarMarksRepository = PlanarMarksRepositoryImpl(planarMarksDao)

val multiPlanarMarksDao = MultiPlanarMarksDao()
val multiPlanarMarksRepository = MultiPlanarMarksRepositoryImpl(multiPlanarMarksDao)