package com.warehouse.api


import akka.http.scaladsl.model.{ContentTypes, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Directive1, Route}
import com.warehouse.domain.{Product, ProductJsonSupport}
import com.warehouse.service.WarehouseService

import scala.util.{Failure, Success}

class WarehouseApiImplementaion(warehouseService: WarehouseService) extends WarehouseApi with ProductJsonSupport {

  private val productsPath: Directive[Unit] = path("warehouse" / "products")
  private val sellPath = path("warehouse" / "product" / Segment)

   override val allProductsRoute: Route = productsPath {
     get {
       pathEndOrSingleSlash {
         val allProducts = warehouseService.getAllAvailableProducts
         complete(allProducts)
       }
     }
   }

  override val productSellRoute: Route = sellPath { productName =>
     post {
       pathEndOrSingleSlash {
         val sold = warehouseService.sellProduct(productName, 1)
         sold match {
           case Success(product) => complete(product)
           case Failure(_) => complete(StatusCodes.NoContent)
         }
       }
     }
  }

  val totalRoute: Route = allProductsRoute ~ productSellRoute

}
