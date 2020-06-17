import dao.*
import kotlinx.coroutines.GlobalScope
import repository.*

val userDao = UserDao()
val userRepository = UserRepositoryImpl(userDao)

val researchDao = ResearchDao()
val researchRepository = ResearchRepositoryImpl(researchDao)

val userResearchDao = UserResearchDao()
val userResearchRepository = UserResearchRepositoryImpl(userResearchDao)

val covidMarksDao = CovidCovidMarkDao()
val covidMarksRepository = CovidCovidMarkRepositoryImpl(covidMarksDao)

val marksDao = MarksDao()
val marksRepository = MarksRepositoryImpl(marksDao)

val creator = ContainerCreatorImpl()
val researchDirFinder = ResearchDirFinderImpl()
val sessionRepository = SessionRepositoryImpl(
  creator,
  researchDirFinder,
  GlobalScope.coroutineContext
)
