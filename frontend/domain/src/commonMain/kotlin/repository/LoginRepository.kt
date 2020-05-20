package repository

interface LoginRepository {
    suspend fun auth(login: String, password: String)
    suspend fun tryToAuth(login: String, password: String)
}