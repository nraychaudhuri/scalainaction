package chap08.typeclasses.xmlconverter

trait XmlConverter[A] {
  def toXml(a: A): String
}

//traditional adapter pattern
object MovieXmlConverter extends XmlConverter[Movie] {
  def toXml(a: Movie) = 
    <movie>
      <name>{a.name}</name>
      <year>{a.year}</year>
      <rating>{a.rating}</rating>
    </movie>.toString
}

case class Movie(name: String, year: Int, rating: Double)

object Converters {
  implicit object MovieConverter extends XmlConverter[Movie] {
    def toXml(a: Movie) = <movie>
                            <name>{a.name}</name>
                            <year>{a.year}</year>
                            <rating>{a.rating}</rating>
                          </movie>.toString  
  }    
}

object SpecialConverters {
  implicit object MovieConverterWithoutRating extends XmlConverter[Movie] {
    def toXml(a: Movie) = <movie>
                            <name>{a.name}</name>
                            <year>{a.year}</year>
                          </movie>.toString  
  }
}

object Main {
  def toXml[A](a: A)(implicit converter: XmlConverter[A]) = converter.toXml(a)    
  def toXml1[A: XmlConverter](a: A) = implicitly[XmlConverter[A]].toXml(a)  

  def toXmlDefault(a: Movie) = {
    import Converters._
    println(toXml(a))       
  }
   
  def toXmlSpecial[A](a: Movie) = {
    import SpecialConverters._
    println(toXml(a))       
  } 
  def main(args: Array[String]): Unit = {
    val p = Movie("Inception", 2010, 10)
    toXmlDefault(p)
    toXmlSpecial(p)
  }  
}  