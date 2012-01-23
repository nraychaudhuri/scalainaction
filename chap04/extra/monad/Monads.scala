// Start the REPL.
// Go into paste mode via >:paste
// Copy the content of this file and past it into the REPL.
// Confirm with crtl+D
// This is necessary, because the REPL ahs problems loading a class/trait and a companion object.

trait Monad[M[_]] {
  def flatMap[A, B](a: M[A], f: A => M[B]): M[B]
  def unital[A](a: A): M[A]
}

// A simple data type, which turns out to satisfy the above trait
case class Inter[A](f: Int => A)
 
// So does this.
case class Identity[A](a: A)

object Monad {
  def ListMonad: Monad[List] = new Monad[List] {
    override def flatMap[A, B](a: List[A], f: A => List[B]): List[B] = {
      a.flatMap(f)
    }

    override def unital[A](a: A): List[A] = {
      List[A](a)
    }
  }
  // // 3. Replace error("todo") with an implementation
  // def OptionMonad: Monad[Option] = error("todo")
  //  
  // // 4. Replace error("todo") with an implementation
  def InterMonad: Monad[Inter] = new Monad[Inter] {
    override def flatMap[A, B](a: Inter[A], f: A => Inter[B]): Inter[B] = {
      val g = a match { case Inter(g) => g }
      Inter((n: Int) => f(g(n)) match { case Inter(fn) => fn(n) })
    }
    override def unital[A](a: A): Inter[A] = {
      Inter[A]({ _ => a })
    }
  }
   

  def IdentityMonad: Monad[Identity] = new Monad[Identity] {
    override def flatMap[A, B](a: Identity[A], f: A => Identity[B]): Identity[B] = {
      f(a.a)
    }

    override def unital[A](a: A): Identity[A] = {
      Identity[A](a)
    }
    
  }
}


// object MonadicFunctions {
//   def sequence[M[_], A](as: List[M[A]], m: Monad[M]): M[List[A]] = 
//     as.foldRight(m.unital(List(): List[A]))((p, q) => m.flatMap(p, (x: A) => m.flatMap(q, (y: List[A]) => m.unital(x :: y))))
// 
//   def fmap[M[_], A, B](a: M[A], f: A => B, m: Monad[M]): M[B] =
//     m.flatMap(a, (x: A) => m.unital(f(x)))
// 
//   def flatten[M[_], A](a: M[M[A]], m:Monad[M]): M[A] =
//     m.flatMap(a, identity[M[A]])
// 
//   def apply[M[_], A, B](f: M[A => B], a: M[A], m: Monad[M]): M[B] =
//     m.flatMap(a, (x: A) => m.flatMap(f, (fn: A => B) => m.unital(fn(x))))
// 
//   def filterM[M[_], A](f: A => M[Boolean], as: List[A], m: Monad[M]): M[List[A]] = 
//     as match {
//       case List()    => m.unital(as)
//       case a :: tail => m.flatMap(f(a), (b: Boolean) => if(b) fmap(filterM(f, tail, m), (a :: (_: List[A])), m) else filterM(f, tail, m))
//     }
// 
//   def replicateM[M[_], A](n: Int, a: M[A], m: Monad[M]): M[List[A]] = 
//     sequence((for(_ <- 1 to 10) yield a).toList, m)
// 
//   def lift2[M[_], A, B, C](f: (A, B) => C, a: M[A], b: M[B], m: Monad[M]): M[C] = 
//     apply(m.flatMap(a, (x: A) => m.unital(f(x, _:B))), b, m)
//   }
// }
// 
