package com.warehouse.domain

import com.warehouse.LoadDB
import com.warehouse.domain._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spray.json._


class InventorySpec extends AnyFlatSpec with Matchers with InventoryJsonSupport {

  "Inventory serialization" should "work as expected" in {
    val loadDB = LoadDB

    val inventoryJson = loadDB.readResource("inventory.json")

    val inventory = inventoryJson.get.parseJson.convertTo[Inventory]

    val expected = Inventory(Seq(Item("1", "leg", 12), Item("2", "screw", 17), Item("3", "seat", 2), Item("4", "table top", 1)))

    inventory shouldEqual expected
  }

  "Inventory" should "remove articles" in {
    val inventory = Inventory(Seq(Item("1", "leg", 12), Item("2", "screw", 17), Item("3", "seat", 2), Item("4", "table top", 1)).sortBy(_.name))
    val article = Article("1", 4)

    val expected = Inventory(Seq(Item("1", "leg", 8), Item("2", "screw", 17), Item("3", "seat", 2), Item("4", "table top", 1)))
    val actual = Inventory(inventory.removeItemStock(article).inventory.sortBy(_.name))

    actual shouldEqual expected
  }

}
