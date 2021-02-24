package model

data class ResearchCSV(
  val accessionNumber: String,
  val studyInstanceUID: String,
  val studyID: String,
  val protocol: String,
  val accessionNumber_base: String,
  val accessionNumber_lastDigit: String,
  val json_name: String,
  val doctor1: String,
  val doctor2: String,
  val pos_in_block: String
)

data class DoctorResearchesCSV(
  val accessionNumber: String,
  val studyInstanceUID: String,
  val studyID: String
)