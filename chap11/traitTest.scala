package test

trait Trait {
  def bar = {}
}

import java.io._
class Reader(fname: String) {
  private val in =
    new BufferedReader(new FileReader(fname))
  @throws(classOf[IOException])
  def read() = in.read()
}