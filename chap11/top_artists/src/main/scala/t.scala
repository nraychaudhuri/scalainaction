import scala.annotation.target._
import reflect.BeanProperty
import javax.persistence._ 

class A {   @(Id @beanGetter) @BeanProperty val x = 0 }
