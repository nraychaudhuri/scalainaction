val suffixes = List("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")

println(ordinal(args(0).toInt))

def ordinal(number:Int) = number match {
  case tenTo20 if 10 to 20 contains tenTo20 => number + "th"
  case rest => rest + suffixes(number % 10)
}
