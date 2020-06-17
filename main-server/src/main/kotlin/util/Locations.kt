package util

import io.ktor.locations.Location
import model.*

@Location(ROOT)
class Index()

@Location("/$LOGIN_ROUTE")
class Login()

@Location("/register")
class Register

@Location("/$RESEARCH_ROUTE/$LIST_ROUTE")
class ResearchesList

@Location("/$RESEARCH_ROUTE/$INIT_ROUTE/{id}")
class InitResearch(val id: Int)

@Location("/$RESEARCH_ROUTE/{id}")
class GetSlice(val id: Int)

@Location("/$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE")
class Hounsfield()

@Location("/$RESEARCH_ROUTE/{id}/$CLOSE_ROUTE")
class CloseSession(val id: Int)

@Location("/$RESEARCH_ROUTE/{id}/$COVID_MARK_ROUTE")
class CovidMark(val id: Int)

@Location("/$RESEARCH_ROUTE/{id}/$MARK_ROUTE")
class CreateMark(val id: Int)

@Location("$RESEARCH_ROUTE/{id}/$MARK_ROUTE/$LIST_ROUTE")
class GetMarks(val id: Int)

@Location("$RESEARCH_ROUTE/$MARK_ROUTE")
class UpdateMark

@Location("$RESEARCH_ROUTE/$MARK_ROUTE/{id}")
class DeleteMark(val id: Int)
