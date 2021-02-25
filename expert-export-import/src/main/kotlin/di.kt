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

val covidMarksDao = CovidMarkDao()
val covidMarksRepository = CovidMarksRepositoryImpl(covidMarksDao)

val multiPlanarMarksDao = MultiPlanarMarksDao()
val planarMarksDao = PlanarMarksDao()
val multiPlanarMarksRepository = MultiPlanarMarksRepositoryImpl(multiPlanarMarksDao)
val planarMarksRepository = PlanarMarksRepositoryImpl(planarMarksDao)

val creator = ContainerCreatorImpl()
val researchDirFinder = ResearchDirFinderImpl()
val sessionRepository = SessionRepositoryImpl(
  creator,
  researchDirFinder,
  GlobalScope.coroutineContext
)

val exportedMarksDao = ExportedMarksDao()
val exportedMarksRepository = ExportedMarksRepositoryImpl(exportedMarksDao)