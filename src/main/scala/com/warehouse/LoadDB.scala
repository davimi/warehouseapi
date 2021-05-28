package com.warehouse

import com.redis.RedisClient
import com.warehouse.data.RedisService
import com.warehouse.domain._
import spray.json._

import scala.io.Source
import scala.util.Try

object LoadDB extends App with DomainJsonFormats {

  val redis = new RedisClient()

  val productsFile = "products.json"
  val inventoryFile = "inventory.json"

  def readResource(resourcePath: String): Try[String] = Try(Source.fromResource(resourcePath).getLines().fold("")(_ + _))

  val productsJson = readResource(productsFile).getOrElse("").parseJson
  val products = productsJson.convertTo[Products]

  val inventoryJson = readResource(inventoryFile).getOrElse("").parseJson
  val inventory = inventoryJson.convertTo[Inventory]

  products.products.foreach { product =>
    redis.sadd(RedisService.productsKey, product.toJson.toString())
  }

  inventory.inventory.foreach { item =>
    redis.sadd(RedisService.inventoryKey, item.toJson.toString())
  }

  println(redis.keys())

}
