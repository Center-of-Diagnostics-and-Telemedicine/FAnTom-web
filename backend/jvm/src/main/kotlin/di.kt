import dao.*
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

val userExpertMarkDao = UserExpertMarkDao()
val userExpertMarkRepository = UserExpertMarkRepositoryImpl(userExpertMarkDao)

val exportedMarksDao = ExpertMarksDao()
val expertMarksRepository = ExpertMarksRepositoryImpl(exportedMarksDao)
