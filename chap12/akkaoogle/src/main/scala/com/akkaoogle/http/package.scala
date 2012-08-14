package com.akkaoogle

package object http {
	import akka.util.duration._
	import akka.util.Timeout
	
	implicit val timeout = Timeout(5 seconds)
}