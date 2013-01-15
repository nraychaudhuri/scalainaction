package HigherKindedTypes 

import scala.language.higherKinds

object Mappers {
  trait Mapper[F[_]] {
      def fmap[A, B](xs: F[A], f: A => B): F[B]   
  }
  def VectorMapper = new Mapper[Vector] {
    def fmap[A, B](xs: Vector[A], f: A => B): Vector[B]= xs map f 
  }
  
  def OptionMapper = new Mapper[Option] {
    def fmap[A, B](r: Option[A], f: A => B): Option[B]= r map f     
  }

  def EitherMapper[X] = new Mapper[({type E[A] = Either[X, A]})#E ] {
    def fmap[A, B](r: Either[X, A], f: A => B): Either[X, B] = r match {
      case Left(a) => Left(a)
      case Right(a) => Right(f(a))
    }     
  }
  
  def Function0Mapper = new Mapper[Function0] {
    def fmap[A, B](r: Function0[A], f: A => B) = new Function0[B] {
      def apply = f(r.apply)
    }
  }
  

}
object Main {
  import Mappers._
  def main(args: Array[String]): Unit = run
  
  def run = {
    println(VectorMapper.fmap(Vector(1, 2, 3), (x: Int) => x + 1))
    println(OptionMapper.fmap(Option(2), (x: Int) => x + 1))
    println(EitherMapper.fmap(Right("one"), (x: String) => x.toUpperCase))
    println(Function0Mapper.fmap(() => "one", (s: String) => s.toUpperCase).apply)
  }
}