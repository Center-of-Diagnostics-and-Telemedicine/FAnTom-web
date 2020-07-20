package util

import io.ktor.application.*
import io.ktor.auth.*
import model.UserModel

const val AUTHENTICATION = "Authentication"
const val NAME_FIELD = "name"
const val PASSWORD_FIELD = "password"
const val ROLE_FIELD = "role"
const val SEEN_FIELD = "seen"
const val DONE_FIELD = "done"
const val MARKED_FIELD = "marked"
const val USER_ID_FIELD = "user_id"
const val RESEARCH_ID_FIELD = "research_id"
const val CT_TYPE_FIELD = "ct_type"
const val LEFT_PERCENT_FIELD = "left_percent"
const val RIGHT_PERCENT_FIELD = "right_percent"

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
const val MODALITY_FIELD = "modality"

const val USER_TABLE = "user"
const val RESEARCH_TABLE = "research"
const val COVID_MARKS_TABLE = "covid_marks"

const val MULTI_PLANAR_MARKS_TABLE = "multi_planar_marks"
const val PLANAR_MARKS_TABLE = "planar_marks"
const val X_FIELD = "x"
const val Y_FIELD = "y"
const val Z_FIELD = "z"
const val RADIUS_HORIZONTAL_FIELD = "radius_horizontal"
const val RADIUS_VERTICAL_FIELD = "radius_vertical"
const val SIZE_FIELD = "size"
const val MARK_TYPE_FILED = "mark_type"
const val CUT_TYPE_FILED = "cut_type"
const val COMMENT_FILED = "comment"


const val ROOT = "/"
const val STATIC_ROUTE = "/static"
const val RESOURCE_STATIC = "static/static"
const val RESOURCE_INDEX = "static/index.html"
const val RESOURCE_JS = "js.js"

val ApplicationCall.user get() = authentication.principal<UserModel>()!!
