package com.warehouse.data

import com.warehouse.domain
import com.warehouse.domain.{Article, Inventory, Item}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DataServiceSpec extends AnyFlatSpec with Matchers {

  private val testItem = Item("1", "screw", 4)

  val dataService = new DataService {
    override def getAllProducts(): Seq[domain.Product] = Seq.empty

    override def getInventory(): Inventory = Inventory(Seq.empty)

    override def getItem(artId: String): Option[Item] = Some(testItem)

    override def updateItem(artId: String, item: Item): Boolean = true
  }

  "Reducing the stock of an item" should "work" in {
    dataService.reduceStockOfItem(testItem, 2).stock shouldEqual 2
  }

  "Reducing the stock of an item" should "not make it negative" in {
    dataService.reduceStockOfItem(testItem, 5).stock shouldEqual 0
  }

  "Updating an Item" should "calculate new stock" in {

    dataService.updateInventory(List(Article("1", 2))) shouldBe true

  }

}
