package util

import ROOT
import io.ktor.locations.*
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

@Location("/$SESSION_ROUTE/$CLOSE_ROUTE")
class CloseSession

@Location("/$RESEARCH_ROUTE/{id}/$CLOSE_ROUTE")
class CloseResearch(val id: Int)

@Location("/$RESEARCH_ROUTE/{id}/$COVID_MARK_ROUTE")
class CovidMark(val id: Int)

@Location("/$RESEARCH_ROUTE/{id}/$MARK_ROUTE")
class CreateMark(val id: Int)

@Location("$RESEARCH_ROUTE/{id}/$MARK_ROUTE/$LIST_ROUTE")
class GetMarks(val id: Int)

@Location("$RESEARCH_ROUTE/{id}/$MARK_ROUTE")
class UpdateMark(val id: Int)

@Location("$RESEARCH_ROUTE/{researchId}/$MARK_ROUTE/{markId}")
class DeleteMark(val researchId: Int, val markId: Int)
