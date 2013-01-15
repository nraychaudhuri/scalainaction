import scala.language.higherKinds

trait Lift[F[_]] {
  // Spot the pattern in these type signatures
  // of increasing arity.
 
  def lift0[A]:
    A => F[A]
 
  def lift1[A, B]:
    (A => B) => (F[A] => F[B])
 
  def lift2[A, B, C]:
    (A => B => C) => (F[A] => F[B] => F[C])
 
  def lift3[A, B, C, D]:
    (A => B => C => D) => (F[A] => F[B] => F[C] => F[D])
 
  // ... and so on
 
  // The relationship between lift<N> and lift<N-1>
  // can be given by a function,
 
  def ap[A, B]:
    F[A => B] => (F[A] => F[B])
}

trait LiftImpl[F[_]] extends Lift[F] {
  // Each lift function uses
  // the previous lift function and ap.
 
  def lift1[A, B]:
    (A => B) => (F[A] => F[B])
    = ap compose lift0
 
  def lift2[A, B, C]:
    (A => B => C) => (F[A] => F[B] => F[C])
    = f => ap compose lift1(f)
 
  def lift3[A, B, C, D]:
    (A => B => C => D) => (F[A] => F[B] => F[C] => F[D])
    = f => a => ap compose lift2(f)(a)
}

class ListLift extends LiftImpl[List] {
  def lift0[A]: A => List[A] = (a) => List(a)
  
  def ap[A, B]: List[A => B] => (List[A] => List[B]) = 
     (xs:List[A=>B]) => (ys: List[A]) => ys.map(xs.head) 
}

class OptionLift extends LiftImpl[Option] {
  def lift0[A]: A => Option[A] = (a) => Some(a)

  def ap[A, B]: Option[A => B] => (Option[A] => Option[B]) = 
     (a:Option[A => B]) => (b: Option[A]) => b.map(a.get)
  
}

// The following compiles in sbt, but the REPL might reject the symbols. 
// You can replace lambda by L and alpha by X and paste it into the REPL.
class EitherLift[R] extends LiftImpl[({type  λ[α] = Either[R, α]})#λ]{ 
  def lift0[A]: A => Either[R, A] = (a) => Right(a)
  
  def ap[A, B]: Either[R, A => B] => (Either[R, A] => Either[R, B]) = 
	(ef:Either[R, A => B]) => (ea:Either[R, A]) => ea.fold(Left(_), a => ef.fold(Left(_), f => Right(f(a))))
}