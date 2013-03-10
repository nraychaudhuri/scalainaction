package checks

import org.scalacheck._

object StringSpecification extends Properties("String") {
  property("reverse of reverse gives you same string back") = 
    Prop.forAll((a: String) => a.reverse.reverse == a)
        
  property("startsWith") = Prop.forAll {(x: String, y: String) =>
      x.startsWith(y) == x.reverse.endsWith(y.reverse)
  }
  
  // property("string comparison - WILL FAIL") = Prop.forAll {(x: String, y: String) =>
  //   x > y == x.reverse > y.reverse   
  // }
}

object EitherSpecification extends Properties("Either") {
  import Gen._
  import Arbitrary.arbitrary
  
  val leftValueGenerator = arbitrary[Int].map(Left(_))
  val rightValueGenerator = arbitrary[Int].map(Right(_))
  implicit val eitherGenerator = oneOf(leftValueGenerator, rightValueGenerator)
  //implicit val eitherGenerator = frequency((3, leftValueGenerator), (1, rightValueGenerator))
  
  // implicit def arbitraryEither[X, Y](implicit xa: Arbitrary[X], ya: Arbitrary[Y]): Arbitrary[Either[X, Y]] = 
  //   Arbitrary[Either[X, Y]](
  //     oneOf(arbitrary[X].map(Left(_)), arbitrary[Y].map(Right(_)))
  //   )

  property("isLeft or isRight not both") = Prop.forAll((e: Either[Int, Int]) => e.isLeft != e.isRight)
  
  property("left value") = Prop.forAll{(n: Int) => Left(n).fold(x => x, b => sys.error("fail")) == n }
  
  property("Right value") = Prop.forAll{(n: Int) => Right(n).fold(b => sys.error("fail"), x => x) == n }
  
  property("swap values") = Prop.forAll{(e: Either[Int, Int]) => e match {
      case Left(a) => e.swap.right.get == a
      case Right(b) => e.swap.left.get == b
    }
  }  
  
  property("getOrElse") = Prop.forAll{(e: Either[Int, Int], or: Int) => e.left.getOrElse(or) == (e match {
      case Left(a) => a
      case Right(_) => or
    })
  }
  
  property("forall") = Prop.forAll {(e: Either[Int, Int]) =>
    e.right.forall(_ % 2 == 0) == (e.isLeft || e.right.get % 2 == 0)
  }
}
