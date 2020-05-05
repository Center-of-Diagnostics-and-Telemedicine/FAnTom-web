package util

import model.User
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import java.io.File

const val AUTHENTICATION = "Authentication"
const val ID_FIELD = "id"
const val NAME_FIELD = "name"
const val PASSWORD_FIELD = "password"
const val ROLE_FIELD = "role"
const val SEEN_FIELD = "seen"
const val DONE_FIELD = "done"
const val MARKED_FIELD = "marked"
const val USER_ID_FIELD = "user_id"
const val RESEARCH_ID_FIELD = "research_id"

const val ACCESSION_NUMBER_FIELD = "accession_number"
const val STUDY_INSTANCE_UID_FIELD = "study_instance_uid"
const val STUDY_ID_FIELD = "study_id"
const val PROTOCOL_FIELD = "protocol"
const val ACCESSION_NUMBER_BASE_FIELD = "accession_number_base"
const val ACCESSION_NUMBER_LAST_DIGIT_FIELD = "accession_number_last_digit"
const val JSON_NAME_FIELD = "json_name"
const val DOCTOR_1_FIELD = "doctor1"
const val DOCTOR_2_FIELD = "doctor2"
const val POS_IN_BLOCK_FIELD = "pos_in_block"

const val USER_TABLE = "user"
const val RESEARCH_TABLE = "research"
const val MARKS_TABLE = "marks"

const val ROOT = "/"
const val STATIC_ROUTE = "/static"
const val RESOURCE_STATIC = "static/static"
const val RESOURCE_INDEX = "static/index.html"


const val data_store_root_path = "/data/Dicom/CT"
val data_store_paths = listOf(
  File("${data_store_root_path}/ChestCT"),
  File("${data_store_root_path}/ChestHCT"),
  File("$data_store_root_path/ChestHCT2")
)
const val csv_store_path = "/data/2020_covid"
const val basePort = 8080
const val fantomServerPort = 8081

val ApplicationCall.user get() = authentication.principal<User>()!!