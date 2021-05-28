package com.warehouse.api

import akka.http.scaladsl.server.Route

trait WarehouseApi {

  val allProductsRoute: Route

  val productSellRoute: Route

}
