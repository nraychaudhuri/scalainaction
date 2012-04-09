sealed trait Request[IN[_]] {
}

sealed trait Response[OUT[_]] {
  val body: OUT[Byte]
}

trait Application[IN[_], OUT[_]] {
  def apply(implicit req: Request[IN]): Response[OUT]
}

object Application {
  def application[IN[_], OUT[_]](f: Request[IN] => Response[OUT]) 
		= new Application[IN,OUT] {
    	def apply(implicit req: Request[IN]) = f(req)
  }
}

Application.application { req: Request[Stream] =>
  new Response[Stream] {
    val message = "hello world"
  }
}
