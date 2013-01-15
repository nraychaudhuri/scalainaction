// Start scala >scala
// load into the REPL> :load FilterAndWithFilterExample.scala
package chap04

object FilterExample extends App {
	val list = List(1, 2, 3)
	var go = true        
	val x = for(i <- list; if(go)) yield {
	  go = false
	  i
	}
	println(x)

	go = true
	val y = list filter { 
	  case i => go
	} map { 
	  case i => {
	    go = false
	    i
	  }
	}          
	println(y)

	go = true
	val z = list withFilter { 
	  case i => go
	} map { 
	  case i => {
	    go = false
	    i
	  }
	}          
	println(z)	
}
