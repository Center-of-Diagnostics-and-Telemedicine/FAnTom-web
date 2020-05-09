package util

import io.ktor.locations.Location
import model.*

@Location("/hello")
class HelloWorld

@Location("/$RESEARCH_ROUTE/$INIT_ROUTE/{name}")
class InitResearch(val name: String)

@Location("/$RESEARCH_ROUTE")
class GetSlice

@Location("/$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE")
class Hounsfield