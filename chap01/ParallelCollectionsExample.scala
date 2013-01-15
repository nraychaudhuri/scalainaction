
object ParallelCollectionsExample extends App {
  def timeOperation(payload: => Unit) = {
  	val start = System.currentTimeMillis
  	payload
  	println("Time taken " + (System.currentTimeMillis - start))
  }

  timeOperation((1 to 1000000).toList.map(_*2))
  timeOperation((1 to 1000000).toList.par.map(_*2))  
}
