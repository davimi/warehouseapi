package com.warehouse.domain

import com.warehouse.LoadDB
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import spray.json._

class ProductSpec extends AnyFlatSpec with Matchers with ProductJsonSupport {

  "Product serialization" should "work as expected" in {
    val loadDB = LoadDB

    val productsJson = loadDB.readResource("products.json")
    val products = productsJson.get.parseJson.convertTo[Products]

    val expected = Products(Seq(Product("Dining Chair", Seq(Article("1", 4), Article("2", 8), Article("3", 1))), Product("Dinning Table", Seq(Article("1", 4), Article("2", 8), Article("4", 1)))))

    products shouldEqual expected
  }

  "Product filtering" should "return the product if enough inventory exists" in {
    val product = Product("Dining Chair", Seq(Article("1", 4), Article("2", 8), Article("3", 1)))
    val inventory = Inventory(List(Item("1", "screw", 5), Item("2", "screw", 8), Item("3", "screw", 2)))

    product.isAvailableBasedOnInventory(inventory) shouldEqual true
  }

  "Product filtering" should "not return the product if an item is missing" in {
    val product = Product("Dining Chair", Seq(Article("1", 4), Article("2", 8), Article("3", 1)))
    val inventory = Inventory(List(Item("1", "screw", 5), Item("2", "screw", 8)))

    product.isAvailableBasedOnInventory(inventory) shouldEqual false
  }

  "Product filtering" should "not return the product if not enough inventory exists" in {
    val product = Product("Dining Chair", Seq(Article("1", 4), Article("2", 8), Article("3", 1)))
    val inventory = Inventory(List(Item("1", "screw", 5), Item("2", "screw", 0), Item("3", "screw", 2)))

    product.isAvailableBasedOnInventory(inventory) shouldEqual false
  }

}
