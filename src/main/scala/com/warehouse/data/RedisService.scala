package com.warehouse.data

import com.warehouse.domain._
import com.warehouse.domain.Product
import com.redis._
import spray.json._

class RedisService(port: Int = 6379) extends DataService with DomainJsonFormats {

  import RedisService._

  val redis = new RedisClient("localhost", port)

  override def getInventory(): Inventory = {
    val inventoryItemKeys: Set[String] = redis.smembers(inventoryArticlesKeys)
      .map( keys =>
        keys.flatten).getOrElse(Set.empty)
    println("Item Keys: " + inventoryItemKeys)
    val items = inventoryItemKeys.flatMap { key =>
      redis.get(generateItemKey(key)).map(item => item.parseJson.convertTo[Item])
    }
    println("Current inventory: " + items.map(_.toString))
    Inventory(items.toSeq)
  }

  override def getAllProducts(): Seq[Product] = {
    val productSet = redis.smembers(productsKey)
      .map( products => products
        .map( product =>
          product.map(_.parseJson.convertTo[Product])
        )
      )
      productSet.getOrElse(Set.empty[Option[Product]]).flatten.toSeq
  }

  override def updateInventory(articles: Seq[Article]): Boolean = {

    articles.map { article =>
      val inventoryItem = redis.get(generateItemKey(article.artId)).map(item => item.parseJson.convertTo[Item])
      val updatedItem = inventoryItem.map(item => item.copy(item.artId, item.name, item.stock - article.amountOf))
      val updated = updatedItem match {
        case Some(item) =>
          redis.set(generateItemKey(article.artId), item.toJson.toString())
        case None => false
      }
      updated
    }.reduce(_ & _)
  }
}

object RedisService {

  val productsKey = "products"
  val inventoryArticlesKeys = "inventory"
  val inventoryItemsPrefix = "item_"

  def generateItemKey(artId: String): String = inventoryItemsPrefix + artId
}
