import java.util.Date
import java.sql.{Date => SqlDate}
import RichConsole._

val now = new Date
p(now)
val sqlDate = new SqlDate(now.getTime)
p(sqlDate)