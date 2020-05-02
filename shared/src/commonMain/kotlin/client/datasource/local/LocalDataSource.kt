package client.datasource.local

interface LocalDataSource {
    fun saveToken(token: String)
    fun getToken(): String
//    fun getFavoriteTalks(): List<Talk>
//    fun saveRate(rate: Rate)
//    fun saveTalk(talk: Talk)
//    fun getRate(talkId: Int): Int
}