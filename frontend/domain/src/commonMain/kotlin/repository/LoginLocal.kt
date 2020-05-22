package repository

interface LoginLocal {
  suspend fun getToken(): String
  suspend fun saveToken(token: String)
}