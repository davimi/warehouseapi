package com.warehouse.service

import com.warehouse.domain
import com.warehouse.domain._

import scala.util.Try

trait WarehouseService {

  def getAllProducts: Seq[Product]

  def getInventory: Inventory

  def sellProduct(productId: Int, quantity: Int): Try[domain.Product]

  def getAllAvailableProducts: Seq[Product] = {
    val inventory = getInventory
    val availableProducts = getAllProducts.filter(prd => prd.isAvailableBasedOnInventory(inventory))
    println("Available products: " + availableProducts)
    availableProducts
  }

}
