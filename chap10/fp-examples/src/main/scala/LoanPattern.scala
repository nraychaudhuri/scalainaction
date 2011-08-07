package chap10.fp.examples

trait Resource { def dispose(): Unit }

class UseResource {
  def use[A, B <: Resource](r: B)(f: B => A): A = { 
    try {
      f(r)
    }finally {
      r.dispose()
    }
  }  
} 

