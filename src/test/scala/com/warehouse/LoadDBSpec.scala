package com.warehouse

import com.warehouse.domain.DomainJsonFormats
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Success

class LoadDBSpec extends AnyFlatSpec with Matchers with DomainJsonFormats {

  "The preloading of the database" should "read files from resources" in {

    val loadDB = LoadDB

    val inventoryJson = loadDB.readResource("inventory.json")
    val productsJson = loadDB.readResource("products.json")

    inventoryJson shouldBe a[Success[_]]
    productsJson shouldBe a[Success[_]]

  }

}
