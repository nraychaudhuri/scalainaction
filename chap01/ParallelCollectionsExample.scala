def timeOperation(payload: => Unit) = {
	val start = System.currentTimeMillis
	payload
	println("Time taken " + (System.currentTimeMillis - start))
}

timeOperation((1 to 1000000).toArray.map(_*2))
timeOperation((1 to 1000000).toArray.par.map(_*2))