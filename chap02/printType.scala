// Load into the REPL in chap02/ via
// scala> :load printType.scala
// and e.g. try scala>rangeMatcher1(101)

def printType(obj: AnyRef) = obj match {
  case s: String => println("This is string")
  case l: List[_] => println("This is List")
  case a: Array[_] => println("This is an array")
  case d: java.util.Date => println("This is a date")
}


def rangeMatcher(num:Int) = num match {
  case within10 if within10 <= 10 => println("within 0 to 10")
  case within100 if within100 <= 100 => println("within 11 to 100")
  case beyond10 if beyond10 < Integer.MAX_VALUE => println("beyond 100")
}  

def rangeMatcher1(num:Int) = num match {
  case within10 if within10 <= 10 => println("within 0 to 10")
  case within100 if within100 <= 100 => println("within 11 to 100")
  case _ => throw new IllegalArgumentException("Only values between 0 and 100 is allowed")
}  

