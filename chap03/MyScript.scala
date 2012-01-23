// Start the REPL and load the file via>:load MyScript.scala

class MyScript(host:String) {
  require(host != null, "Have to provide host name")
  if(host == "127.0.0.1") println("host = localhost") 
  else println("host = " + host)
}
