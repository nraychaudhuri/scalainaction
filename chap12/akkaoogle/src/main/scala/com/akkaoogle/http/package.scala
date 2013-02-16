package com.akkaoogle

package object http {
  import java.util.concurrent.TimeUnit
  import akka.util.Timeout

	implicit val timeout = Timeout(500, TimeUnit.MILLISECONDS)
}