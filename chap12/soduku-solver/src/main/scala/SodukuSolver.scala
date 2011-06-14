
case class Square(row1: List[Int], row2: List[Int], row3: List[Int]) {

  private def newRow(oldRow: List[Int], num: Int) = {
    oldRow match {
      case List(0, x, y) => List(num, x, y)
      case List(x, 0, y) => List(x, num, y)
      case List(x, y, 0) => List(x, y, num)
      case _ => Nil
    }
  }

  private def first: PartialFunction[Int, Square] = {
    case num if row1.contains(0) =>
      Square(newRow(row1, num), row2, row3  )
  }

  private def second: PartialFunction[Int, Square] = {
    case num if row2.contains(0) =>
      Square(row1, newRow(row2, num), row3)
  }

  private def third: PartialFunction[Int, Square] = {
    case num if row3.contains(0) =>
      Square(row1, row2, newRow(row3, num))
  }

  def apply(i: Int) = {
    val x: PartialFunction[Int, Square] = first orElse second orElse third
    x(i)
  }
}

object SodukuSolver {
  def solve(soduku: Square) = {
    (1 to 9).foldLeft(soduku) { (s: Square, i: Int) => s.apply(i)}
  }
}

