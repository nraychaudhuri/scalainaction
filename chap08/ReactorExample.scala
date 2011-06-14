import scala.actors._, Actor._

case class EmailRequest(email: String, subject: String, text: String)
class EMailSender extends Reactor[EmailRequest] {
  def act = {
    react {
      case e: EmailRequest => println("received email req " + e)
    }
  }
}

val a = new EMailSender
a.start

a ! EmailRequest("a", "b", "c")

val a1 = reactor {
  println("1111")
}

a1 ! EmailRequest("a", "b", "c")