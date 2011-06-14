def loopTill(cond: => Boolean)(body: => Unit): Unit = {
  if (cond) { 
    body
    loopTill(cond)(body)     
  }
}
   
var i = 10   
loopTill (i > 0) {     
   println(i)
   i -= 1   
} 
