package com.warehouse.warehouselogic


import com.warehouse.domain
import com.warehouse.domain._

import scala.util.{Failure, Success, Try}

trait WarehouseService {

  def getAllProducts: Seq[Product]

  def getInventory: Inventory

  def updateInventory(articles: Seq[Article]): Boolean

  def sellProduct(productName: String, quantity: Int): Try[Product] = {

    val productAvailable = getAllAvailableProducts.filter(p => p.name == productName)

    productAvailable.headOption match {

      case Some(product) =>
        if (updateInventory(product.contain_articles)) {
          Success(product)
        } else Failure(new Exception(s"Could not update item stock properly"))

      case None => Failure(new Exception(s"Product $productName not available"))
    }
  }

  def getAllAvailableProducts: Seq[Product] = {
    val inventory = getInventory
    val availableProducts = getAllProducts.filter(product => product.isAvailableBasedOnInventory(inventory))
    println("Available products: " + availableProducts)
    availableProducts
  }

}
