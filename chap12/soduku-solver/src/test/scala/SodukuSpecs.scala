
import org.specs2.mutable.Specification

class SodukuSpec extends Specification {

  "Soduku solver" should {
    "only generate only 1 to 9 once in a square" in {
       val soduku = Square(
          List(0, 0, 0),
          List(0, 0, 0),
          List(0, 0, 0)
       )
       val solved = SodukuSolver.solve(soduku)
       solved must beEqualTo(Square(
         List(1, 2, 3),
         List(4, 5, 6),
         List(7, 8, 9)
       ))
    }
  }
}