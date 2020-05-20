package local

interface LoginLocal {
    fun getToken()
    fun saveToken(token: String)
}

expect class LoginLocalDataSource(): LoginLocal