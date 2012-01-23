// I recommend to build the sbt project chap02/restclient which will download any necessary dependencies.
// 
// To run this in the REPL, first copy httpclient-4.1.2.jar, httpcore-4.1.2.jar, commons-logging-1.1.1.jar to chap02 
// (e.g. from ~/.ivy2/cache/org.apache.httpcomponents or the internet)
// Then run with either on Linux/Unix
// >rest.sh get https://github.com/nraychaudhuri/scalainaction/raw/master/chap02/RestClient.scala
// or in Windows
// >rest.bat get https://github.com/nraychaudhuri/scalainaction/raw/master/chap02/RestClient.scala
//
// The general command line is 
// rest.sh (post | get | delete | options) -d <request parameters comma separated -h <headers comma separated> <url>
// at minimum you should specify action(post, get, delete, options) and url

import org.apache.http._
import org.apache.http.client.entity._
import org.apache.http.client.methods._
import org.apache.http.impl.client._
import org.apache.http.client.utils._
import org.apache.http.message._  
import org.apache.http.params._  



def parseArgs(args: Array[String]): Map[String, List[String]] = {

  def nameValuePair(paramName: String) = {
    def values(commaSeperatedValues: String) = commaSeperatedValues.split(",").toList
    
    val index = args.indexWhere(_ == paramName)
    (paramName, if(index == -1) Nil else values(args(index + 1)))
  }
  
  Map(nameValuePair("-d"), nameValuePair("-h"))
}

def splitByEqual(nameValue:String): Array[String] = nameValue.split('=') 

def headers = for(nameValue <- params("-h")) yield {  
  def tokens = splitByEqual(nameValue)
  new BasicHeader(tokens(0), tokens(1))
}

def formEntity = {
  def toJavaList(scalaList: List[BasicNameValuePair]) = {
    java.util.Arrays.asList(scalaList.toArray:_*)
  }

  def formParams = for(nameValue <- params("-d")) yield {
    def tokens = splitByEqual(nameValue)
    new BasicNameValuePair(tokens(0), tokens(1))
  }

  def formEntity = new UrlEncodedFormEntity(toJavaList(formParams), "UTF-8")
  formEntity
}

def handlePostRequest = {
  val httppost = new HttpPost(url)
  headers.foreach { httppost.addHeader(_) }
  httppost.setEntity(formEntity)
  val responseBody = new DefaultHttpClient().execute(httppost, new BasicResponseHandler())
  println(responseBody)
}

def handleGetRequest = {
  val query = params("-d").mkString("&")
  val httpget = new HttpGet(url + "?" + query)
  headers.foreach { httpget.addHeader(_) }
  val responseBody = new DefaultHttpClient().execute(httpget, new BasicResponseHandler())
  println(responseBody)
}

def handleDeleteRequest = {
  val httpDelete = new HttpDelete(url)
  val httpResponse = new DefaultHttpClient().execute(httpDelete)
  println(httpResponse.getStatusLine())  
}

def handleOptionsRequest = {
  val httpOptions = new HttpOptions(url)
  headers.foreach { httpOptions.addHeader(_) }
  val httpResponse = new DefaultHttpClient().execute(httpOptions)
  println(httpOptions.getAllowedMethods(httpResponse))
}

require(args.size >= 2, "at minimum you should specify action(post, get, delete, options) and url")
val command = args.head
val params = parseArgs(args)
val url = args.last

command match {
  case "post"    => handlePostRequest
  case "get"     => handleGetRequest
  case "delete"  => handleDeleteRequest
  case "options" => handleOptionsRequest
}
