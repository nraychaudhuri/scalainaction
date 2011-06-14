
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

