package com.warehouse.domain

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsObject, JsString, JsValue, RootJsonFormat}

case class Item(artId: String, name: String, stock: Int)

case class Inventory(inventory: Seq[Item]) {

  def removeItemStock(article: Article): Inventory = {
    val (matchingItems, otherItems) = inventory.span(_.artId == article.artId)
    val updatedItem = matchingItems.headOption match {
      case Some(i) if i.stock >= article.amountOf => List(Item(i.artId, i.name, i.stock - article.amountOf))
      case Some(_) => matchingItems // should never occur
      case None => matchingItems
    }
    val newInventory = otherItems.toList ::: updatedItem.toList
    this.copy(newInventory)
  }
}

trait InventoryJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with ItemJsonFormat {
  import ItemJsonProtocol._
  implicit val inventoryFormat = jsonFormat1(Inventory)
}

trait ItemJsonFormat extends DefaultJsonProtocol {

  implicit object ItemJsonProtocol extends RootJsonFormat[Item] {
    override def write(item: Item): JsValue = {
      JsObject(
        "art_id" -> JsString(item.artId),
        "name" -> JsString(item.name),
        "stock" -> JsString(item.stock.toString)
      )
    }

    override def read(json: JsValue): Item = {
      json.asJsObject.getFields("art_id", "name", "stock") match {
        case Seq(JsString(art_id), JsString(name), JsString(stock)) => Item(art_id, name, stock.toInt)
        case s => throw DeserializationException(s"Could not read json of ${Item.getClass.getSimpleName}: $s")
      }
    }
  }
}