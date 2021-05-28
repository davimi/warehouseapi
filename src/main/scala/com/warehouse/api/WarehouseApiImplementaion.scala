package com.warehouse.api


import akka.http.scaladsl.model.{ContentTypes, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Directive1, Route}
import com.warehouse.domain.{Product, ProductJsonSupport}
import com.warehouse.service.WarehouseService
import spray.json.enrichAny

class WarehouseApiImplementaion(warehouseService: WarehouseService) extends WarehouseApi with ProductJsonSupport {

  private val productsPath: Directive[Unit] = path("warehouse" / "products")
  private val sellPath: Directive[Tuple1[Int]] = path("warehouse" / "product" / IntNumber)

   override val allProductsRoute: Route = productsPath {
     get {
       pathEndOrSingleSlash {
         val allProducts = warehouseService.getAllAvailableProducts
         complete(allProducts)
       }
     }
   }

  override val productSellRoute: Route = sellPath { productId =>
     post {
       pathEndOrSingleSlash {
         complete(StatusCodes.OK)
       }
     }
  }

  val totalRoute: Route = allProductsRoute ~ productSellRoute

}
