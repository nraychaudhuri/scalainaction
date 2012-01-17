// Start the scala REPL and start the paste mode via >:paste
// copy the content of this file and paste it in the REPL
// finish with CRTL-D

abstract class Role { def canAccess(page: String): Boolean }
class Root extends Role { override def canAccess(page:String) = true }
class SuperAnalyst extends Role { override def canAccess(page:String) = page != "Admin" }
class Analyst extends Role { override def canAccess(page:String) = false }

object Role {
  def apply(roleName:String) = roleName match {
    case "root" => new Root
    case "superAnalyst" => new SuperAnalyst
    case "anaylst" => new Analyst
  }
}