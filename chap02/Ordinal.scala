// Run with e.g. >scala Ordinal.scala 9 
// in chap02/

ordinal(args(0).toInt)

def ordinal(number:Int) = number match {
  case 1 => println("1st")
  case 2 => println("2nd")
  case 3 => println("3rd")
  case 4 => println("4th")
  case 5 => println("5th")
  case 6 => println("6th")
  case 7 => println("7th")
  case 8 => println("8th")
  case 9 => println("9th")
  case 10 => println("10th")
  case _ => println("Cannot do beyond 10")
}