package chap08.expression.problem

trait Base {
  type exp <: Exp
  
  trait Exp {
    def eval: Int
  }
  
  class Num(v: Int) extends Exp {
    def value = v
    def eval  = value
  }
}

trait BasePlus extends Base {
  class Plus(l: exp, r: exp) extends Exp {
    val left  = l
    val right = r
    def eval  = left.eval + right.eval 
  }
}

trait BaseNeg extends Base { 
  class Neg(t: exp) extends Exp { 
    val term = t
    def eval = - term.eval
  } 
}

trait BasePlusNeg extends BasePlus with BaseNeg


trait Show extends Base {

  type exp <: Exp
  
  trait Exp extends super.Exp {
    def show: String
  }
  
  class Num(v: Int) extends super.Num(v) with Exp {
    def show = value.toString
  }
}

trait ShowPlusNeg extends BasePlusNeg with Show {
  class Plus(l: exp, r: exp) extends super.Plus(l, r) with Exp {
    def show = left.show + " + " + right.show
  }
  
  class Neg(t: exp) extends super.Neg(t) with Exp {
    def show = "-(" + term.show + ")"
  }
}

// usage example
object Main extends ShowPlusNeg {
      
	 type exp = Exp // this is path dependent type Show.Exp not to be mistaken for Show#Exp, which would not compile. 
	
	println("type exp is now: " + classOf[exp])
	
	def main(args:Array[String]) {	
		
		val e:exp = new Plus( 
			new Num(27)
			, new Plus(
				new Neg(new Num(13))
				, new Num(28)
				)
			)				 
			
		println(e.show + " = " + e.eval )
	}
}


