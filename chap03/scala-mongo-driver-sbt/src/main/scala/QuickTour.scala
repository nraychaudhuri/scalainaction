import com.scalainaction.mongo._
import com.mongodb.BasicDBObject

object QuickTour extends App{

	def client = new MongoClient
	def db = client.db("mydb")
	for(name <- db.collectionNames) println(name)

	val col = db.readOnlyCollection("test")
	println(col.name)

	val adminCol = db.administrableCollection("test")
	adminCol.drop

	val updatableCol = db.updatableCollection("test")

	val doc = new BasicDBObject()
	doc.put("name", "MongoDB")
	doc.put("type", "database")
	doc.put("count", 1)

	val info = new BasicDBObject()
	info.put("x", 203)
	info.put("y", 102)
	doc.put("info", info)
	updatableCol += doc

	println("1---" + updatableCol.findOne)

	updatableCol -= doc
	println("2---" + updatableCol.findOne)

	for(i <- 1 to 100) updatableCol += new BasicDBObject("i", i)

	var query = new BasicDBObject
	query.put("i", 71);
	var cursor = col.find(query)
	while(cursor.hasNext()) {
	  println(cursor.next());
	}

	// var rangeQuery = new BasicDBObject("i", new BasicDBObject("$gt", 20))
	// var richQuery = Query(rangeQuery).skip(20).limit(10)
	// cursor = col.find(richQuery)
	// while(cursor.hasNext()) {
	//   println(cursor.next());
	// }

}








 
