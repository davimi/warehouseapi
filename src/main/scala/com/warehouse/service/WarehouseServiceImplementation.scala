package com.warehouse.service
import com.warehouse.data.DataService
import com.warehouse.domain
import com.warehouse.domain.{Article, Inventory}

import scala.util.{Failure, Try}

class WarehouseServiceImplementation(database: DataService) extends WarehouseService {

  override def getAllProducts: Seq[domain.Product] = database.getAllProducts()

  override def getInventory: Inventory = database.getInventory()

  override def updateInventory(articles: Seq[Article]): Boolean = database.updateInventory(articles)
}
