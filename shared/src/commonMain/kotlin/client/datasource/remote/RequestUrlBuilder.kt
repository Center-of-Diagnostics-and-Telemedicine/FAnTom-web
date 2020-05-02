package client.datasource.remote

class RequestUrlBuilder {

    var url: String = ""
    private val and = "&"
    private val equal = "="

    fun appendEndPoint(endPoint: String): RequestUrlBuilder {
        url.plus("$endPoint?")
        return this
    }

    fun appendRoute(route: String): RequestUrlBuilder {
        url.plus("/$route")
        return this
    }

    fun appendParam(param: String, value: String): RequestUrlBuilder {
        url.plus(param).plus(equal).plus(value).plus(and)
        return this
    }
}