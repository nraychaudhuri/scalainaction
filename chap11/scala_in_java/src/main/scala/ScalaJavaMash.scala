package chap11.scala

trait Persistable[T]{
  def getEntity: T
  def save(): T = {
    persistToDb(getEntity)
    getEntity
  }
  private def persistToDb(t: T) = {}
}


trait RemoteLogger extends java.rmi.Remote {
  @throws(classOf[java.rmi.RemoteException])
  def log(m: String)
} 