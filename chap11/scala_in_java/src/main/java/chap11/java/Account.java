package chap11.java;

import chap11.scala.*;

public class Account implements Persistable<Account> {
  public Account getEntity() { return this; }
  public Account save() {
    return (Account)Persistable$class.save(this);
  }
}