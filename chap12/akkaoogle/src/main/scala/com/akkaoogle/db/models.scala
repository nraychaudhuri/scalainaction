package com.akkaoogle.db

import AkkaoogleSchema._
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import java.util.Date

package object models {
  implicit val transactionFailures: Table[TransactionFailure] = AkkaoogleSchema.transactionFailures
  implicit val vendors: Table[ExternalVendor] = AkkaoogleSchema.vendors
  implicit val products: Table[Product] = AkkaoogleSchema.products
}

package models {
  trait Model[A] extends KeyedEntity[Long] { this: A =>
    val id: Long = 0
    def save(implicit table: Table[A]): Either[Throwable, String] = {
      tx {
        try {
          table.insert(this)
          Right("Domain object is saved successfully")
        } catch {
          case exception: Throwable => Left(exception)
        }
      }
    }
  }

  class TransactionFailure(val vendorId: String,
                       val message: String,
                       val timestamp: Date) extends Model[TransactionFailure]

  object TransactionFailure {
    def findAll = tx { from(transactionFailures)(s => select(s)) map(s => s) }
  }

  class ExternalVendor(val name: String, val url: String) extends Model[ExternalVendor]

  object ExternalVendor {
    def findAll = tx { from(vendors)(s => select(s)) map(s => s) }
  }

  class Product(val description: String,
                val vendorName: String,
                val basePrice: Double,
                val plusPercent: Double)
    extends Model[Product] {
    def calculatePrice = basePrice + (basePrice * plusPercent / 100)
  }

  object Product {
    def findByDescription(description: String): Option[Product] =
      tx {
        products.where(p => p.description like description).headOption
      }
  }
}

