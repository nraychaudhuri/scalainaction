package com.akkaoogle.http

import com.typesafe.play.mini._
import play.api.mvc._
import play.api.mvc.Results._
import com.akkaoogle.infrastructure._
import akka.pattern.{ ask, pipe, AskTimeoutException }
import com.akkaoogle.calculators.messages._
import play.api.libs.concurrent._
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * this application is registered via Global
 */
object App extends Application { 
  def route = {
	  case GET(Path("/")) => Action { request =>
		  Ok(views.index()).as("text/html") 
	  }
	  case GET(Path("/akkaoogle/search"))  & QueryString(qs) => Action { request =>
		  val desc = QueryString(qs, "productDescription").get.asScala
		  val f =
        (AkkaoogleActorServer.lookup("cheapest-deal-finder-balancer") ? FindPrice(desc.head, 1)).mapTo[Option[LowestPrice]]
		  val result = f.map({
				case Some(lowestPrice)=> 
					Ok(lowestPrice.toString).as("text/html")
				case _ => 
				  Ok("No price found").as("text/html")
			})
      AsyncResult(result)
	  }
  }
}
