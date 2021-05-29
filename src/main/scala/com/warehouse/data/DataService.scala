package com.warehouse.data

import com.warehouse.domain._

trait DataService {

  def getAllProducts(): Seq[Product]

  def getInventory(): Inventory

  def getItem(artId: String): Option[Item]

  def updateItem(artId: String, item: Item): Boolean

  def updateInventory(articles: Seq[Article]): Boolean = {

    articles.map { article =>
      val inventoryItem = getItem(article.artId)

      val updated = inventoryItem match {
        case Some(item) =>
          val updatedItem = reduceStockOfItem(item, article.amountOf)
          updateItem(article.artId, updatedItem)
        case None => false
      }
      updated
    }.reduce(_ & _)
  }

  private[data] def reduceStockOfItem(item: Item, amount: Int) = item.copy(item.artId, item.name, math.max(0, item.stock - amount))

}
