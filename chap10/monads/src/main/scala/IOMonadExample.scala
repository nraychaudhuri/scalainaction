package chap10.monads.example

object IOMonadExample {
  trait WorldState {def nextState: WorldState }
}