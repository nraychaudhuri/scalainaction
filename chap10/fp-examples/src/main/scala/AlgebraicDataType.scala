package chap10.fp.examples


object ADT {
  sealed trait Account
  case class CheckingAccount(accountId: String) extends Account
  case class SavingAccount(accountId: String, limit: Double) extends Account
  case class PremiumAccount(corporateId: String, accountHolder: String) extends Account

  def printAccountDetails(account: Account): Unit = account match {
    case CheckingAccount(accountId) => println("Account id " + accountId)
    case SavingAccount(accountId, limit) => println("Account id " + accountId + " , " + limit)
//    case PremiumAccount(corporateId, accountHolder) => println("Corporate id " + corporateId + " , " + accountHolder)
  }  
}
