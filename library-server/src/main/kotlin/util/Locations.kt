package util

import io.ktor.locations.Location
import model.HOUNSFIELD_ROUTE
import model.INIT_ROUTE
import model.RESEARCH_ROUTE

@Location("/hello")
class HelloWorld

@Location("/$RESEARCH_ROUTE/$INIT_ROUTE/{name}")
class InitResearch(val name: String)

@Location("/$RESEARCH_ROUTE/{id}")
class GetSlice(val id: Int)

@Location("/$RESEARCH_ROUTE/{name}/$HOUNSFIELD_ROUTE")
class Hounsfield(val name: String)