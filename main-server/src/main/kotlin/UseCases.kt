import io.ktor.auth.*
import model.DX_RESEARCH_TYPE
import model.UserModel
import model.UserRole
import model.hash
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

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
    SchemaUtils.createMissingTablesAndColumns(
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

//  seedDBWithDicoms()
//  adminForAllResearches()
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

fun adminForAllResearches() {
  return transaction {
    ResearchVos.selectAll().asSequence().forEach { row ->
      UserResearchVos.insertIgnore {
        it[userId] = 1
        it[researchId] = row[ResearchVos.id].toInt()
        it[seen] = 0
        it[done] = 0
      }
    }
  }
}

fun seedDBWithDicoms() {
  val path = "C:\\dicom\\out"
  val dir = File(path)
  if (dir.isDirectory) {
    dir.list().forEach { name ->
      val fileName = name.split(".").first()
      transaction {
        ResearchVos.insertIgnore {
          it[accessionNumber] = fileName
          it[studyInstanceUID] = fileName
          it[studyID] = fileName
          it[protocol] = fileName
          it[accessionNumber_base] = fileName
          it[accessionNumber_lastDigit] = fileName
          it[json_name] = fileName
          it[doctor1] = fileName
          it[doctor2] = fileName
          it[pos_in_block] = fileName
          it[modality] = DX_RESEARCH_TYPE
        }
      }
    }
  }
}
