package com.warehouse.domain

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._


case class Article(artId: String, amountOf: Int)

case class Product(name: String, contain_articles: Seq[Article]) {

  def isAvailableBasedOnInventory(inventory: Inventory): Boolean = {
    val inventoryMap = inventory.inventory.foldLeft(Map.empty[String, Int])( (map, item) => map + (item.artId -> item.stock))
    contain_articles.forall(article => inventoryMap.getOrElse(article.artId, -1) >= article.amountOf)
  }

}

case class Products(products: Seq[Product])


trait ProductJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with ArticleJsonFormat {
  import ArticleJsonProtocol._

  implicit val productFormat = jsonFormat2(Product)
  implicit val productsFormat = jsonFormat1(Products)
}

trait ArticleJsonFormat extends DefaultJsonProtocol {

  implicit object ArticleJsonProtocol extends RootJsonFormat[Article] {
    override def write(article: Article): JsValue = {
      JsObject(
        "art_id" -> JsString(article.artId),
        "amount_of" -> JsString(article.amountOf.toString)
      )
    }

    override def read(json: JsValue): Article = {
      json.asJsObject.getFields("art_id", "amount_of") match {
        case Seq(JsString(art_id), JsString(amount_of)) => Article(art_id, amount_of.toInt)
        case s => throw DeserializationException(s"Could not read json of ${Item.getClass.getSimpleName}: $s")
      }
    }
  }
}