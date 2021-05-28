package com.warehouse.data

import com.warehouse.domain._

trait DataService {

  def getAllProducts(): Seq[Product]

  def getInventory(): Inventory
}
