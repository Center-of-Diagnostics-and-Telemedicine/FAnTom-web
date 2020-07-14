import io.ktor.auth.*
import model.UserModel
import model.UserRole
import model.hash
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun init() {
  transaction {
    SchemaUtils.create(
      UserVos,
      ResearchVos,
      UserResearchVos,
      CovidMarksVos,
      MultiPlanarMarksVos,
      PlanarMarksVos
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

      UserVos.insertIgnore {
        it[name] = "Expert1"
        it[password] = hash("4qmp")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert2"
        it[password] = hash("2b1p")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert3"
        it[password] = hash("nxsr")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert4"
        it[password] = hash("y7br")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert5"
        it[password] = hash("2h92")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert6"
        it[password] = hash("x43d")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert7"
        it[password] = hash("07dt")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert8"
        it[password] = hash("g6pz")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert9"
        it[password] = hash("shf2")
        it[role] = UserRole.DOCTOR.value
      }
      UserVos.insertIgnore {
        it[name] = "Expert10"
        it[password] = hash("92fl")
        it[role] = UserRole.DOCTOR.value
      }
    }
  }
}

fun getUser(id: Int): UserModel = transaction {
  UserVos.select {
    UserVos.id eq id
  }.first()
}.toUser()

fun getUserByCredentials(credentials: UserPasswordCredential): UserModel? {
  return transaction {
    UserVos.select {
      UserVos.name eq credentials.name and (UserVos.password eq hash(credentials.password))
    }.firstOrNull()
  }?.toUser()
}
