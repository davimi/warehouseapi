package com.warehouse.warehouselogic

import com.warehouse.domain
import com.warehouse.domain.{Article, Inventory, Item, Product, Products}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success}

class WarehouseServiceSpec extends AnyFlatSpec with Matchers {

  val warehouseService = new WarehouseService {
    override def getAllProducts: Seq[domain.Product] = Seq(Product("Dining Chair", Seq(Article("1", 4), Article("2", 8), Article("3", 1))), Product("Dinning Table", Seq(Article("1", 4), Article("2", 8), Article("4", 1))))

    override def getInventory: Inventory = Inventory(Seq(Item("1", "leg", 12), Item("2", "screw", 17), Item("3", "seat", 2), Item("4", "table top", 1)))

    override def updateInventory(articles: Seq[Article]): Boolean = true
  }

  "The warehouse" should "sell products" in {
    val sold = warehouseService.sellProduct("Dining Chair", 1)

    sold shouldEqual Success(warehouseService.getAllProducts.head)
  }

  "The warehouse" should "not sell products which do not exist" in {
    val sold = warehouseService.sellProduct("car", 1)

    sold.isFailure shouldEqual true
  }
}
