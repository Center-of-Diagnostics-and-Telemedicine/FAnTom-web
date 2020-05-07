import io.ktor.auth.UserPasswordCredential
import model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import util.csv_store_path
import util.database
import java.io.*

fun init() {
  database {
    SchemaUtils.create(
      UserVos,
      ResearchVos,
      UserResearchVos,
      MarkVos
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

//  val researchesCsv = parseResearches()
//  createResearches(researchesCsv)
//  for (i in 0..9) {
//    val docResearchCSV = parseDoctorResearches(i)
//    updateResearchUser(docResearchCSV, i + 1)
//  }
}

fun getUser(id: Int): UserModel = database {
  UserVos.select {
    UserVos.id eq id
  }.first()
}.toUser()

fun getUserByCredentials(credentials: UserPasswordCredential): UserModel? {
  return database {
    UserVos.select {
      UserVos.name eq credentials.name and (UserVos.password eq hash(credentials.password))
    }.firstOrNull()
  }?.toUser()
}

fun parseResearches(): List<ResearchCSV> {
  val csvFile = "$csv_store_path/keys_db.txt"
  var br: BufferedReader? = null
  var line = ""
  val resultList = mutableListOf<ResearchCSV>()

  try {
    br = BufferedReader(FileReader(csvFile))
    while (br.readLine()?.also { line = it } != null) { // use comma as separator
      if (line.isNotEmpty() && line != "\u0000") {
        val fields = line.split(' ', '\t').toTypedArray()
        resultList.add(
          ResearchCSV(
            accessionNumber = fields[0].replace("яю", ""),
            studyInstanceUID = fields[1],
            studyID = fields[2],
            protocol = fields[3],
            accessionNumber_base = fields[4],
            accessionNumber_lastDigit = fields[5],
            json_name = fields[6],
            doctor1 = fields[7],
            doctor2 = fields[8],
            pos_in_block = fields[9]
          )
        )
      }
    }
  } catch (e: FileNotFoundException) {
    e.printStackTrace()
  } catch (e: IOException) {
    e.printStackTrace()
  } finally {
    if (br != null) {
      try {
        br.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
  return resultList
}

fun createResearches(researchesCsv: List<ResearchCSV>): List<Int> = database {
  ResearchVos.batchInsert(data = researchesCsv, ignore = true) { researchCsv ->
    this[ResearchVos.accessionNumber] = researchCsv.accessionNumber
    this[ResearchVos.studyInstanceUID] = researchCsv.studyInstanceUID
    this[ResearchVos.studyID] = researchCsv.studyID
    this[ResearchVos.protocol] = researchCsv.protocol
    this[ResearchVos.accessionNumber_base] = researchCsv.accessionNumber_base
    this[ResearchVos.accessionNumber_lastDigit] = researchCsv.accessionNumber_lastDigit
    this[ResearchVos.json_name] = researchCsv.json_name
    this[ResearchVos.doctor1] = researchCsv.doctor1
    this[ResearchVos.doctor2] = researchCsv.doctor2
    this[ResearchVos.pos_in_block] = researchCsv.pos_in_block
  }.map { it[ResearchVos.id] }
}

fun parseDoctorResearches(number: Int): List<DoctorResearchesCSV> {
  val csvFile = "$csv_store_path/list_doctor_$number.txt"
  var br: BufferedReader? = null
  var line = ""
  val resultList = mutableListOf<DoctorResearchesCSV>()

  try {
    br = BufferedReader(FileReader(csvFile))
    while (br.readLine()?.also { line = it } != null) { // use comma as separator
      if (line.isNotEmpty() && line != "\u0000") {
        val fields = line.split(' ', '\t').toTypedArray()
        resultList.add(
          DoctorResearchesCSV(
            accessionNumber = fields[0].replace("яю", ""),
            studyInstanceUID = fields[1],
            studyID = fields[2]
          )
        )
      }
    }
  } catch (e: FileNotFoundException) {
    e.printStackTrace()
  } catch (e: IOException) {
    e.printStackTrace()
  } finally {
    if (br != null) {
      try {
        br.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
  return resultList
}

fun updateResearchUser(
  docResearchesCSV: List<DoctorResearchesCSV>,
  doctorNumber: Int
) {
  database {
    UserVos.select {
      UserVos.name eq "Expert${doctorNumber}"
    }
      .firstOrNull()
      ?.toUser()
      ?.let { user ->
        docResearchesCSV.forEach { docResearchCSV ->
          ResearchVos.select {
            (ResearchVos.accessionNumber eq docResearchCSV.accessionNumber) and (ResearchVos.studyID eq docResearchCSV.studyID) and (ResearchVos.studyInstanceUID eq docResearchCSV.studyInstanceUID)
          }.toList().forEach { row ->
            UserResearchVos.insertIgnore {
              it[userId] = user.id
              it[researchId] = row[ResearchVos.id]
              it[seen] = 0
              it[done] = 0
            }
          }
        }
      }
  }
}
