import model.User
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser(): User = User(
  id = this[UserVos.id],
  name = this[UserVos.name],
  password = this[UserVos.password],
  role = this[UserVos.role]
)