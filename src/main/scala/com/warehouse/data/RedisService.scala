package com.warehouse.data

import com.warehouse.domain._
import com.warehouse.domain.{Product => DomainProduct}
import com.redis._
import com.warehouse.data.RedisService.{inventoryItemsPrefix, productsKey}
import spray.json._

class RedisService(port: Int = 6379) extends DataService with DomainJsonFormats {

  val redis = new RedisClient("localhost", port)

  override def getInventory(): Inventory = {
    val inventorySet: Set[Option[Item]] = redis.smembers(RedisService.inventoryKey)
      .map( items =>
        items.map( item =>
          item.map(_.parseJson.convertTo[Item])
        )
      ).getOrElse(Set.empty[Option[Item]])

    println("Current inventory: " + Inventory(inventorySet.flatten.toSeq))
    Inventory(inventorySet.flatten.toSeq)
  }

  override def getAllProducts(): Seq[DomainProduct] = {
    val productSet = redis.smembers(productsKey)
      .map( products => products
        .map( product =>
          product.map(_.parseJson.convertTo[DomainProduct])
        )
      )
      productSet.getOrElse(Set.empty[Option[DomainProduct]]).flatten.toSeq
  }

}

object RedisService {

  val productsKey = "products"
  val inventoryKey = "inventory"
  val inventoryItemsPrefix = "item_"

}
