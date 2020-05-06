package model

data class ResearchModel(
  val id: Int,
  val accessionNumber: String,
  val studyInstanceUID: String,
  val studyID: String,
  val protocol: String,
  val accessionNumberBase: String,
  val accessionNumberLastDigit: String,
  val jsonName: String,
  val doctor1: String,
  val doctor2: String,
  val posInBlock: String
)