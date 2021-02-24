import dao.*
import kotlinx.coroutines.GlobalScope
import repository.*

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
