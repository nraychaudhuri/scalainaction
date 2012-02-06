// Compile via >scalac Packages.scala
// Make available in the REPL via >scala -cp .

package com.persistence {
  package mongo {
     class MongoClient
  }  
  package riak {
    class RiakClient  
  }
  package hadoop {
    class HaDoopClient
  }
}

package monads { class IOMonad } 

package io {
  package monads {
    class Console { val m = new _root_.monads.IOMonad }
  }
}

