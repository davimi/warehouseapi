package com.warehouse.service
import com.warehouse.data.DataService
import com.warehouse.domain
import com.warehouse.domain.Inventory

import scala.util.{Failure, Try}

class WarehouseServiceImplementation(inventoryService: DataService) extends WarehouseService {

  override def getAllProducts: Seq[domain.Product] = inventoryService.getAllProducts()

  override def sellProduct(productId: Int, quantity: Int): Try[domain.Product] = Failure(new Exception("fail"))

  override def getInventory: Inventory = inventoryService.getInventory()
}
