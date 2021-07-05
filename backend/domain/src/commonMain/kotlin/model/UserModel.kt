package model

interface UserModel {
  val id: Int
  val name: String
  val password: String
  val role: Int
}