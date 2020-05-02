import model.User
import model.hash
import model.Research
import io.ktor.auth.UserPasswordCredential
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun init() {
  transaction {
    SchemaUtils.create(
      UserVos,
      ResearchVos,
      UserResearchVos
    )
    val login = "admin"
    val pass = hash("vfrcbv16")
    val userByCredentials = getUserByCredentials(UserPasswordCredential(login, pass))
    if (userByCredentials == null) {
      UserVos.insertIgnore {
        it[name] = login
        it[password] = pass
        it[role] = UserRole.ADMIN.value
      }
    }
  }
}

fun createUser(login: String, pass: String, userRole: Int): Int = transaction {
  UserVos.insert {
    it[name] = login
    it[password] = pass
    it[role] = userRole
  } get UserVos.id
}

fun getUser(id: Int): User = transaction {
  UserVos.select {
    UserVos.id eq id
  }.first()
}.toUser()

fun getUserByCredentials(credentials: UserPasswordCredential): User? {
  return transaction {
    UserVos.select {
      UserVos.name eq credentials.name and (UserVos.password eq hash(credentials.password))
    }.firstOrNull()
  }?.toUser()
}

fun getResearches(userId: Int): List<Research> {
  return transaction {
    UserResearchVos.select {
      UserResearchVos.userId eq userId
    }.toList()
  }.map {
    val id = it[UserResearchVos.researchId]
    val seen = it[UserResearchVos.seen]
    val done = it[UserResearchVos.done]

    Research(
      id = id,
      name = transaction {
        ResearchVos.select { ResearchVos.id eq id }.first()
      }.let { result -> result[ResearchVos.name] },
      seen = seen == 1,
      done = done == 1,
      marked = isMarked(userId, id)
    )
  }
}

fun getResearch(researchId: Int, userId: Int): Research {
  return transaction {
    ResearchVos.select {
      ResearchVos.id eq researchId
    }.first()
  }.let {
    val userResearchResultRow = transaction {
      UserResearchVos.select {
        (UserResearchVos.userId eq userId) and (UserResearchVos.researchId eq researchId)
      }.first()
    }
    Research(
      id = it[ResearchVos.id],
      name = it[ResearchVos.name],
      seen = userResearchResultRow[UserResearchVos.seen] == 1,
      done = userResearchResultRow[UserResearchVos.done] == 1,
      marked = isMarked(userId, researchId)
    )
  }
}

fun updateResearches(accessionNumbers: List<String>) {
  transaction {
    accessionNumbers.forEach { accessionNumber ->
      ResearchVos.insertIgnore {
        it[name] = accessionNumber
      }
    }
    val users = UserVos.selectAll().map { row -> row.toUser() }
    val researches = ResearchVos.selectAll().map { resultRow -> resultRow[ResearchVos.id] }
    users.forEach { user ->
      researches.forEach { resId ->
        UserResearchVos.insertIgnore {
          it[userId] = user.id
          it[researchId] = resId
        }

      }
    }
  }
}

fun updateResearch(research: Research, userId: Int) = transaction {
  ResearchVos
    .update(where = { ResearchVos.id eq research.id }) {
      it[name] = research.name
    }
  UserResearchVos
    .update(where = { UserResearchVos.userId eq userId and (UserResearchVos.researchId eq research.id) }) {
      it[seen] = if (research.seen) 1 else 0
      it[done] = if (research.done) 1 else 0
    }
//  getResearch(researchId = research.id, userId = userId)
}

fun isMarked(userrId: Int, researchhId: Int): Boolean = transaction {
  false
//  MarkVos.select {
//    MarkVos.userId eq userrId and (MarkVos.researchId eq researchhId)
//  }.firstOrNull() != null
}
