package chap10.fp.examples

import scala.annotation.tailrec

object RecursionExamples {
  def sum(xs: List[Int]): Int = xs match {
    case Nil => 0
    case x :: ys => x + sum(ys)
  }
  
  def removeDups[A](xs: List[A]): List[A] = xs match {
    case Nil => Nil
    case x :: ys if(ys.contains(x)) => removeDups(ys)
    case x :: ys => removeDups(ys) :+ x
  }
  
  def exists[A](elem: A, xs: List[A]): Boolean = xs match {
    case Nil => false
    case x :: ys => (elem == x) || exists(elem, ys)
  }
  
  def length[A](xs: List[A]): Int = xs match {
    case Nil => 0
    case x :: ys => 1 + length(ys)
  }
  
  
  def length2[A](xs: List[A]): Int = {
    @tailrec 
    def _length(xs: List[A], currentLength: Int): Int = xs match {
      case Nil => currentLength
      case x :: ys => _length(ys, currentLength + 1)      
    }
    _length(xs, 0)
  }
  
  def unfoldR[A, B](seed: B)(f: B => Option[(A, B)]): List[A] = f(seed) match {
    case Some((a, b)) => a :: unfoldR(b)(f)
    case None => Nil
  }
  
  def unfoldL[A, B](seed: B)(f: B => Option[(B, A)]): List[A] = {
    def loop(seed: B)(ls: List[A]): List[A] = f(seed) match {
      case Some((b, a)) => loop(b)(a :: ls)
      case None => ls
    }
    loop(seed)(Nil)
  }
  
  def foldL[A, B]: ((B, A) => B) => B => List[A]=> B = (f) => (initial) => (xs) => {
    xs.foldLeft(initial)(f)
  }
  
  def factors: Int => Option[(Int, Int)] = (n) => if(n==0) None else Some((n-1, n))
  def multiply: (Int, Int) => Int = _ * _
  
  def fact(n: Int) = foldL(multiply)(1)(unfoldL(n)(factors))  
}
