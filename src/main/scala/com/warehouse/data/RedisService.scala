package com.warehouse.data

import com.warehouse.domain._
import com.warehouse.domain.Product
import com.redis._
import spray.json._

class RedisService(port: Int = 6379) extends DataService with DomainJsonFormats {

  import RedisService._

  val redis = new RedisClient("localhost", port)

  override def getItem(artId: String): Option[Item] = redis.get(generateItemKey(artId)).map(item => item.parseJson.convertTo[Item])

  override def updateItem(artId: String, item: Item): Boolean = redis.set(generateItemKey(artId), item.toJson.toString())

  override def getInventory(): Inventory = {
    val inventoryItemKeys: Set[String] = redis.smembers(inventoryArticlesKeys)
      .map( keys =>
        keys.flatten).getOrElse(Set.empty)

    val items = inventoryItemKeys.flatMap { key =>
      getItem(key)
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
}

object RedisService {

  val productsKey = "products"
  val inventoryArticlesKeys = "inventory"
  val inventoryItemsPrefix = "item_"

  def generateItemKey(artId: String): String = inventoryItemsPrefix + artId
}
