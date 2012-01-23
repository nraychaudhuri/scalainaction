// Start the REPL and load the file with> :load Classes.scala

class AddressBean(var address1:String, var address2:String, city:String, zipCode:Int)

class MongoClient(val host:String, val port:Int) {
  def this() = { 
    this("defaultHost", 123)
    println("hhh")
  }
}  





