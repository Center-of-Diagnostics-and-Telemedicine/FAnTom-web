package local

import repository.LoginLocal

actual object LoginLocalDataSource : LoginLocal {
    override suspend fun getToken(): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(token: String) {
        TODO("Not yet implemented")
    }
}