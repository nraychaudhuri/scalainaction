package com.scalainaction.mongo

import com.mongodb.{DB => MongoDB}
import scala.collection.convert.Wrappers._

class DB private(val underlying: MongoDB) {  
  private def collection(name: String) = underlying.getCollection(name)

  def localeAwareReadOnlyCollection(name: String) = 
      new DBCollection(collection(name)) with Memoizer with LocaleAware
  def readOnlyCollection(name: String) = 
      new DBCollection(collection(name)) with Memoizer
  def administrableCollection(name: String) = 
      new DBCollection(collection(name)) with Administrable with Memoizer
  def updatableCollection(name: String) = 
      new DBCollection(collection(name)) with Updatable with Memoizer  
  def collectionNames =  for(name <- new JSetWrapper(underlying.getCollectionNames)) yield name
}

object DB {
  def apply(underlying: MongoDB) = new DB(underlying)
}
